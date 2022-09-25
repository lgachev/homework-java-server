package homework.javaserver.service;

import homework.javaserver.controller.dto.PythonDTO;
import homework.javaserver.socket.WSHandler;
import homework.javaserver.socket.dto.CSSMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO add unit tests for this class, there is a lot ot be tested ;)
@Service
public class PythonService {
    private static Logger logger = LoggerFactory.getLogger(PythonService.class);
    private static final String ERROR_REGEX_VALIDATION_INTERNAL = "The regex validations failed to prevent unprocessable template. Please fix them. ";
    private static final String ERROR_REGEX_VALIDATION = "Unprocessable background color. ";
    private static final String ERROR_REGEX_MORE_THAN_ONE_RGB = "More than one rgb() found. ";
    private static final String ERROR_REGEX_OTHER_THAN_THREE_COLORS = "We have found more or less than three color values. ";

    // Black and white colors are the two extreme values in the relative luminance therefore one of them can create the
    // best possible contrast with any color. Worth noting that nothing is in a really good contrast with some
    // 'mid-range' grey hues. See pickContrastColor for link to the definition.
    private static final double BLACK_RELATIVE_LUMINANCE = calculateRelativeLuminance8Bit(0, 0, 0);
    private static final double WHITE_RELATIVE_LUMINANCE = calculateRelativeLuminance8Bit(255, 255, 255);

    private static final String RGB_BACKGROUND_PATTERN_FINDER = "rgb *\\(( *\\d{1,3} *,{1} *\\d{1,3} *,{1} *\\d{1,3} *)\\)";
    public static final String RGB_BACKGROUND_PATTERN = ".*" + RGB_BACKGROUND_PATTERN_FINDER + ".*";
    public static final String RGB_TEXT_PATTERN = ".*rgb *\\(( *\\{red} *,{1} *\\{green} *,{1} *\\{blue} *)\\).*";

    @Autowired
    WSHandler wsHandler;

    public void updateFrontendCss(PythonDTO pythonDTO) throws Exception {
        CSSMessage message = new CSSMessage();
        message.setTimestamp(pythonDTO.getTimestamp());
        message.setCssBackgroundColor(pythonDTO.getCssBackgroundColor());
        message.setText(pythonDTO.getText());

        int[] colors8bit = getRGBColorsFromBackgroundCss(pythonDTO.getCssBackgroundColor());

        String textColor = pickContrastColor(colors8bit[0], colors8bit[1], colors8bit[2]);
        String textColorTemplate = pythonDTO.getCssTextColorTemplate();

        String textColorResult = textColorTemplate
            .replaceAll("\\{red}", textColor)
            .replaceAll("\\{green}", textColor)
            .replaceAll("\\{blue}", textColor);

        message.setCssTextColor(textColorResult);

        wsHandler.broadcast(pythonDTO.getSessionId(), message);
    }

    /* https://www.w3.org/TR/WCAG21/#dfn-contrast-ratio defines contrast ratios, the higher the better, according to the
    same article contrast below 4.5 (Success Criterion 1.4.3 Contrast (Minimum)§) is unacceptable, and above 7 (Success
    Criterion 1.4.6 Contrast (Enhanced) is good */
    private String pickContrastColor(int r8bit, int g8bit, int b8bit) {
        double backgroundRL = calculateRelativeLuminance8Bit(r8bit, g8bit, b8bit);
        double whiteContrast = (WHITE_RELATIVE_LUMINANCE + 0.05) / (backgroundRL + 0.05);
        double blackContrast = (backgroundRL + 0.05) / (BLACK_RELATIVE_LUMINANCE + 0.05);
        return whiteContrast > blackContrast ? "255" : "0";
    }

    /* https://www.w3.org/TR/WCAG21/#dfn-relative-luminance */
    private static double calculateRelativeLuminance8Bit(int r8bit, int g8bit, int b8bit) {
        double rsRGB = r8bit / 255.0;
        double gsRGB = g8bit / 255.0;
        double bsRGB = b8bit / 255.0;

        return 0.2126 * (rsRGB < 0.03928 ? rsRGB / 12.92 : Math.pow((rsRGB + 0.055) / 1.055, 2.4))
            + 0.7152 * (gsRGB < 0.03928 ? gsRGB / 12.92 : Math.pow((gsRGB + 0.055) / 1.055, 2.4))
            + 0.0722 * (bsRGB < 0.03928 ? bsRGB / 12.92 : Math.pow((bsRGB + 0.055) / 1.055, 2.4));
    }

    private int[] getRGBColorsFromBackgroundCss(String backgroundCss) {
        try {
            Pattern regex = Pattern.compile(RGB_BACKGROUND_PATTERN_FINDER);
            Matcher regexMatcher = regex.matcher(backgroundCss);
            String colors = null;
            while (regexMatcher.find()) {
                if (colors == null) {
                    colors = regexMatcher.group(1);
                } else {
                    throw new RuntimeException(ERROR_REGEX_MORE_THAN_ONE_RGB);
                }
            }
            if (colors == null) {
                throw new RuntimeException(ERROR_REGEX_OTHER_THAN_THREE_COLORS);
            }
            String[] colorsArray = colors.split(",");
            if (colorsArray.length != 3) {
                throw new RuntimeException(ERROR_REGEX_OTHER_THAN_THREE_COLORS);
            }
            return Arrays.stream(colorsArray).mapToInt(color -> Integer.parseInt(color.trim())).toArray();
        } catch (RuntimeException e) {
            logger.error(ERROR_REGEX_VALIDATION_INTERNAL + backgroundCss + " " + e.getMessage());
            throw new IllegalArgumentException(ERROR_REGEX_VALIDATION + backgroundCss + " " + e.getMessage());
        }
    }
}
