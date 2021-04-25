package ch.uzh.ifi.hase.soprafs21.GameEntities;


import ch.uzh.ifi.hase.soprafs21.constant.PlayerClass;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.network.Network;
import ch.uzh.ifi.hase.soprafs21.service.StationService;

import javax.persistence.*;
import java.util.concurrent.ThreadLocalRandom;


@Entity
@Table(name= "GAME")
public class Game {

    @Id
    private Long gameId;

    public Long getGameId() {
        return gameId;
    }
    public void setGameId(Long id) {
        this.gameId = id;
    }

    @OneToOne
    private PlayerGroup playerGroup;

    @OneToOne
    private Network network;

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    // make private and delete InitGame class
    public void addToPlayerGroup(Player player){
        playerGroup.add(player);
    }

    public Game initializeGame(Lobby lobby, Network network){
        // copies lobbyId to gameId
        this.setGameId(lobby.getLobbyId());
        this.setNetwork(network);

        int lobbySize = lobby.getNumberOfPlayers();

        //random number that decides who is MrX
        int whoIsMrX = ThreadLocalRandom.current().nextInt(0, lobbySize);
        int i = 0;
        for (User currUser: lobby.getUsers()){

            Player newPlayer = new Player();
            newPlayer.setPlayerId(currUser.getUserId());
            newPlayer.setUser(currUser);
            newPlayer.setCurrentStation(network.getRandomStation());

            // PlayerClass gets assigned by Random number
            if(i == whoIsMrX){
                newPlayer.setPlayerClass(PlayerClass.MRX);
            } else {
                newPlayer.setPlayerClass(PlayerClass.AGENT);
            }
            this.addToPlayerGroup(newPlayer);
            i++;
        }
        return this;
    }


}
