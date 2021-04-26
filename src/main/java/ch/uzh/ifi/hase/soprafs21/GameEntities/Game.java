package ch.uzh.ifi.hase.soprafs21.GameEntities;


import ch.uzh.ifi.hase.soprafs21.constant.PlayerClass;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.network.Network;
import ch.uzh.ifi.hase.soprafs21.service.StationService;

import javax.persistence.*;
import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;


@Entity
@Table(name= "GAME")
public class Game {

    @Id
    private Long gameId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lobbyId")
    @MapsId
    private Lobby lobby;

    @OneToOne(cascade = CascadeType.ALL)
    private PlayerGroup playerGroup;

    public void addToPlayerGroup(Player player){
        playerGroup.add(player);
    }
    public Player findCorrespondingPlayer(User user){
        return playerGroup.findCorrespondingPlayer(user);
    }
    public PlayerGroup getPlayerGroup() {
        return playerGroup;
    }
    public void setPlayerGroup(PlayerGroup playerGroup) {
        this.playerGroup = playerGroup;
    }

    public Lobby getLobby() {
        return lobby;
    }
    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public Long getGameId() {
        return gameId;
    }
    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}
