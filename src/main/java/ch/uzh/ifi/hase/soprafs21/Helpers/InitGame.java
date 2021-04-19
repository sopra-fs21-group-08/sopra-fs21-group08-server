package ch.uzh.ifi.hase.soprafs21.Helpers;

import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Player;
import ch.uzh.ifi.hase.soprafs21.constant.PlayerClass;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.concurrent.ThreadLocalRandom;

public class InitGame {
    /**
     *
     * @param lobby
     * @return a new game Entity, with the information from the Lobby
     */
    public static Game getNewGame(Lobby lobby){

        Game game = new Game();
        // copies lobbyId to gameId
        game.setGameId(lobby.getLobbyId());

        int lobbySize = lobby.getNumberOfPlayers();

        //random number that decides who is MrX
        int whoIsMrX = ThreadLocalRandom.current().nextInt(0, lobbySize);
        int i = 0;
        for (User currUser: lobby.getUsers()){

            Player newPlayer = new Player();
            newPlayer.setPlayerId(currUser.getUserId());
            newPlayer.setUser(currUser);

            // PlayerClass gets assigned by Random number
            if(i == whoIsMrX){
                newPlayer.setPlayerClass(PlayerClass.MRX);
            } else {
                newPlayer.setPlayerClass(PlayerClass.AGENT);
            }
            game.addToPlayerGroup(newPlayer);
            i++;
        }



        return game;
    }
}
