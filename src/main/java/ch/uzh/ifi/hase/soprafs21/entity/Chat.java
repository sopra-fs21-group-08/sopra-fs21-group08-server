package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="CHAT")
public class Chat {

    @Id
    private Long chatId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "lobbyId")
    @MapsId
    private Lobby lobby;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
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

    public Lobby getLobby() {
        return lobby;
    }
    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }


}
