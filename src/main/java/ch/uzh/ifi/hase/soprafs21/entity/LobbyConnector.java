package ch.uzh.ifi.hase.soprafs21.entity;


import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;

import javax.persistence.*;

@Entity
@Table(name= "NEXTLOBBY")
public class LobbyConnector {

    @Id
    private Long lastLobbyId;

    @OneToOne
    private Lobby next;

    public void setLastLobbyId(Long lastLobbyId) {
        this.lastLobbyId = lastLobbyId;
    }

    public Lobby getNext() {
        return next;
    }
    public void setNext(Lobby next) {
        this.next = next;
    }
}
