package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.network.Network;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name= "LOBBY")
public class Lobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lobbyId;

    @Column
    private String lobbyName;

    @OneToMany(cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Game game;

    @OneToOne(cascade = CascadeType.ALL)
    private Chat chat;

    public Long getLobbyId() {
        return lobbyId;
    }
    public void setLobbyId(Long id) {
        this.lobbyId = id;
    }

    public String getLobbyName() {
        return lobbyName;
    }
    public void setLobbyName(String name) {
        this.lobbyName = name;
    }

    public List<User> getUsers() {
        return users;
    }
    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User userToRemove) {
        this.users.remove(userToRemove);
    }
    public int getSize(){
        return users.size();
    }
    public boolean isEmpty(){
        return this.users.isEmpty();
    }

    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }

    public Chat getChat(){return this.chat;}
    public void setChat(Chat chat){this.chat = chat;}

    public int getNumberOfPlayers() {
        return this.users.size();
    }
}

