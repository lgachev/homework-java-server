package homework.javaserver.controller.dto;

import homework.javaserver.service.PythonService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class PythonDTO {
    @NotBlank(message = "Missing sessionId.")
    private String sessionId;

    @NotNull(message = "Missing timestamp.")
    private Long timestamp;

    @NotBlank(message = "Missing background color.")
    @Pattern(regexp = PythonService.RGB_BACKGROUND_PATTERN, message = "Malformed background color css.")
    private String cssBackgroundColor;

    @NotBlank(message = "Missing text message css template.")
    @Pattern(regexp = PythonService.RGB_TEXT_PATTERN, message = "Malformed text color css.")
    private String cssTextColorTemplate;

    @NotNull(message = "Missing text message.")
    private String text;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCssBackgroundColor() {
        return cssBackgroundColor;
    }

    public void setCssBackgroundColor(String cssBackgroundColor) {
        this.cssBackgroundColor = cssBackgroundColor;
    }

    public String getCssTextColorTemplate() {
        return cssTextColorTemplate;
    }

    public void setCssTextColorTemplate(String cssTextColorTemplate) {
        this.cssTextColorTemplate = cssTextColorTemplate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
