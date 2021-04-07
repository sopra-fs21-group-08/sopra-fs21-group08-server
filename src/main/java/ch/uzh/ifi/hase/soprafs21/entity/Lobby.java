package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.util.ArrayList;


@Entity
@Table(name= "LOBBY")
public class Lobby {



    @Id
    @GeneratedValue
    private Long id;


    @Column(unique = true)
    private String name;

    @JoinColumn
    @CollectionTable(name ="LobbyMembers")
    private ArrayList<User> users = new ArrayList<>();


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<User> getUsers() {
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


    //TODO figgure out why this doesnt work with basic entity chat

    /*
    @OneToOne
    private Chat chat;

    public Chat getChat(){return this.chat;}
    public void setChat(Chat chat){this.chat = chat;}
    */


}

