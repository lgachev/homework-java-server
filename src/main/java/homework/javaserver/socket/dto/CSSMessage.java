package homework.javaserver.socket.dto;

public class CSSMessage {
    private Long timestamp;

    private String cssBackgroundColor;

    private String cssTextColor;

    private String text;

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

    public String getCssTextColor() {
        return cssTextColor;
    }

    public void setCssTextColor(String cssTextColor) {
        this.cssTextColor = cssTextColor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
