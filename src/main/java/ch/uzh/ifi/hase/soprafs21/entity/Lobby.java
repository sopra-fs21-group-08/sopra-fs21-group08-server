package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;

import javax.persistence.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name= "LOBBY")
public class Lobby {

    // dont know exactly what this is used for but copied it from the
    // user class
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long lobbyId;

    @Column(unique = true)
    private String name;

    @OneToMany
    private List<User> users = new ArrayList<>();

    @OneToOne
    private Game game;

    @OneToOne(mappedBy = "lobby")
    private Chat chat;

    public Long getLobbyId() {
        return lobbyId;
    }
    public void setLobbyId(Long id) {
        this.lobbyId = id;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }
    public void addUser(User user) {
        this.users.add(user);
    }
    public void removeUser(User user) {
        int indexToRemove = users.indexOf(user);
        this.users.remove(indexToRemove);
        this.users.add(user);
    }


    public Chat getChat(){return this.chat;}
    public void setChat(Chat chat){this.chat = chat;}



}

