package homework.javaserver.socket;

public class SessionNotFoundException extends Exception {
    private static final String ERROR_MESSAGE = "There is no active websocket with id: ";

    private String sessionId;

    public SessionNotFoundException(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMessage() {
        return ERROR_MESSAGE + sessionId;
    }
}
