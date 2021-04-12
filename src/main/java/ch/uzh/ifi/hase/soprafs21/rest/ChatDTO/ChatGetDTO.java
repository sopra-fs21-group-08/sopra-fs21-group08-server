package ch.uzh.ifi.hase.soprafs21.rest.ChatDTO;

import ch.uzh.ifi.hase.soprafs21.entity.Message;

import java.util.List;

public class ChatGetDTO {

    public List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }


}
