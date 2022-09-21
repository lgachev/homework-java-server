package homework.javaserver.socket;

import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class BasicSessionHandler {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    Map<String, WebSocketSession> getSessions() {
        return sessions;
    }
}
