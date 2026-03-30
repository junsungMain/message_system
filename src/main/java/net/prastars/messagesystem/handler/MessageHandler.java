package net.prastars.messagesystem.handler;

import net.prastars.messagesystem.contants.Constants;
import net.prastars.messagesystem.dto.domain.Message;
import net.prastars.messagesystem.dto.websocket.inbound.BaseRequest;
import net.prastars.messagesystem.dto.websocket.inbound.KeepAliveRequest;
import net.prastars.messagesystem.dto.websocket.inbound.MessageRequest;
import net.prastars.messagesystem.entity.MessageEntity;
import net.prastars.messagesystem.repository.MessageRepository;
import net.prastars.messagesystem.service.SessionService;
import net.prastars.messagesystem.session.WebSocketSessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class MessageHandler extends TextWebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WebSocketSessionManager webSocketSessionManager;
    private final SessionService sessionService;
    private final MessageRepository messageRepository;

    public MessageHandler(WebSocketSessionManager webSocketSessionManager, MessageRepository messageRepository, SessionService sessionService) {
        this.webSocketSessionManager = webSocketSessionManager;
        this.messageRepository = messageRepository;
        this.sessionService = sessionService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("ConnectionEstablished: {}", session.getId());

        ConcurrentWebSocketSessionDecorator concurrentWebSocketSessionDecorator =
                new ConcurrentWebSocketSessionDecorator(session, 5000, 100 * 1024);
        webSocketSessionManager.storeSession(concurrentWebSocketSessionDecorator);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("TransportError: [{}] from {}", exception.getMessage(), session.getId());
        webSocketSessionManager.terminateSession(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("ConnectionClosed: [{},{}] from {}", status.getReason(), status.getCode(), session.getId());
        webSocketSessionManager.terminateSession(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession senderSession, @NonNull TextMessage message) {
        String payload = message.getPayload();
        log.info("Received TextMessage: [{}] from {}", payload, senderSession.getId());
        try {
            BaseRequest baseRequest = objectMapper.readValue(payload, BaseRequest.class);

            if(baseRequest instanceof MessageRequest messageRequest){
                Message receivedMessage = new Message(messageRequest.getUsername(), messageRequest.getContent());
                messageRepository.save(new MessageEntity(receivedMessage.username(), receivedMessage.content()));
                webSocketSessionManager.getSessions().forEach(participantSession -> {
                    if(!senderSession.getId().equals(participantSession.getId())){
                        sendMessage(participantSession,receivedMessage);
                    }
                });
            }else if (baseRequest instanceof KeepAliveRequest keepAliveRequest){
                sessionService.refreshTTL((String) senderSession.getAttributes().get(Constants.HTTP_SESSION_ID.getValue()));
            }
        } catch (Exception ex) {
            String errorMessage = "유효한 프로토콜이 아닙니다.";
            log.error("errorMessage payload: {} from {}", payload, senderSession.getId());
            sendMessage(senderSession, new Message("system", errorMessage));
        }
    }

    private void sendMessage(WebSocketSession session, Message message) {
        try{
            String msg = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(msg));
            log.info("send message: {} to {}", msg, session.getId());
        }catch (Exception e){
            log.error("메시지 전송 실패 to {} error: {}", session.getId(), e.getMessage());
        }
    }
}
