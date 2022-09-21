package homework.javaserver.controller;

public class PythonDTO {
    private String sessionId;

    private Long timestamp;

    private String cssBackgroundColor;

    private String cssTextColorTemplate;

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
