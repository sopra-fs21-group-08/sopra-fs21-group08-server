package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.io.Serial;
import java.util.List;

@Entity
@Table(name="CHAT")
public class Chat {

    private static final long serialVersionUID = 1L;

    /*
    private List<String> messages;
    */

    @Id
    @GeneratedValue
    private Long chatId;

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
    public Long getChatId() {
        return chatId;
    }
}
