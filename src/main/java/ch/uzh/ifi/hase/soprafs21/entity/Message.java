package ch.uzh.ifi.hase.soprafs21.entity;


import javax.persistence.*;

@Entity
@Table("MESSAGE")
public class Message {


    @Id
    @GeneratedValue
    private Long messageId;

    @Column(nullable = false)
    private String message;


    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getMessageId() {
        return messageId;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
