package ch.uzh.ifi.hase.soprafs21.entity;


import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name= "MESSAGE")
public class Message {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column
    private String username;

    @Column(nullable = false)
    public String message;

    @Column
    public final String time = LocalTime.now().toString();

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }


    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
