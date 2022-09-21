package homework.javaserver.service;

import homework.javaserver.controller.PythonDTO;
import homework.javaserver.socket.WSHandler;
import homework.javaserver.socket.dto.CSSMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO add unit tests for this class, there is a lot ot be tested ;)
@Service
public class PythonService {
    // Black and white colors are the two extreme values in the relative luminance therefore one of them can create the
    // best possible contrast with any color. Worth noting that nothing is in a really good contrast with some
    // 'mid-range' grey hues. See pickContrastColor for link to the definition.
    private static double BLACK_RELATIVE_LUMINANCE = calculateRelativeLuminance8Bit(0, 0, 0);
    private static double WHITE_RELATIVE_LUMINANCE = calculateRelativeLuminance8Bit(255, 255, 255);

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

    // TODO add a exception that would resolve to 422 Unprocessable Entity, just vary the messages according to case
    private int[] getRGBColorsFromBackgroundCss(String backgroundCss) {
        Pattern regex = Pattern.compile("rgb\\(([^()]*)\\)");
        Matcher regexMatcher = regex.matcher(backgroundCss);
        String colors = null;
        while (regexMatcher.find()) {
            if (colors == null) {
                colors = regexMatcher.group(1);
                // TODO throw exception if colors still == null, no rgb(*) found
            } else {
                // TODO throw exception, more than one rgb(*) found
            }
        }
        String[] colorsArray = colors.split(",");
        if (colorsArray.length != 3) {
            // TODO throw exception, more or less than 3 RGB values found
        }
        // TODO handle NumberFormatException
        return Arrays.stream(colorsArray).mapToInt(color -> Integer.parseInt(color.trim())).toArray();
    }
}
