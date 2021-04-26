package ch.uzh.ifi.hase.soprafs21.rest.ChatDTO;


import java.util.List;

public class ChatGetDTO {

    public List<MessageGetDTO> messages;

    public List<MessageGetDTO> getMessages() {
        return messages;
    }
    public void setMessages(List<MessageGetDTO> messages) {
        this.messages = messages;
    }


}
