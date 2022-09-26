package homework.javaserver.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import homework.javaserver.socket.dto.CSSMessage;
import homework.javaserver.socket.dto.SessionIdMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

@Component
public class WSHandler extends TextWebSocketHandler {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private BasicSessionHandler sessionHandler;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String request = message.getPayload();
        String response = String.format("response from server to '%s'", HtmlUtils.htmlEscape(request));
        session.sendMessage(new TextMessage(response));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionHandler.getSessions().put(session.getId(), session);

        SessionIdMessage message = new SessionIdMessage();
        message.setSessionId(session.getId());
        session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));

        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionHandler.getSessions().remove(session.getId());
        super.afterConnectionClosed(session, status);
    }

    public void broadcast(String sessionId, CSSMessage message) throws IOException, SessionNotFoundException {
        WebSocketSession session = sessionHandler.getSessions().get(sessionId);
        if (session == null || !session.isOpen()) {
            sessionHandler.getSessions().remove(sessionId);
            throw new SessionNotFoundException(sessionId);
        }
        session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
    }
}
