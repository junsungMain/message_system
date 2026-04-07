package net.prastars.messagesystem.dto.websocket.inbound;

import com.fasterxml.jackson.annotation.JsonCreator;
import net.prastars.messagesystem.contant.MessageType;

public class KeepAliveRequest extends BaseRequest {

    @JsonCreator
    public KeepAliveRequest(String type) {
        super(MessageType.KEEP_ALIVE);
    }
}
