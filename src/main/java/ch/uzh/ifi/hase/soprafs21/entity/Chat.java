package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="CHAT")
public class Chat {

    @Id
    @GeneratedValue
    private Long chatId;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Message> messages = new ArrayList<>();

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
    public Long getChatId() {
        return chatId;
    }

    public void addMessage(Message msg){
        this.messages.add(msg);
    }


}
