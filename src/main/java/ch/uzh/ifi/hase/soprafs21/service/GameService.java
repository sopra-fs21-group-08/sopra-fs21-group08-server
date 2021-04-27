package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.PlayerGroup;
import ch.uzh.ifi.hase.soprafs21.constant.PlayerClass;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }


    public Game initializeGame(Lobby lobby){

        Game game = new Game();
        game.setLobby(lobby);

        //transforming user list into a player list
        PlayerGroup pg = createPlayerGroup(lobby.getUsers());
        pg.setGame(game);
        game.setPlayerGroup(pg);

        game = gameRepository.save(game);
        gameRepository.flush();
        return game;

    }

    public Game getGameByEntity(Game game) {
        return this.gameRepository.findByGameId(game.getGameId());
    }

    /**
     *
     * @param users
     * @return a Playergroup of a list of users
     */
    private PlayerGroup createPlayerGroup(List<User> users){
        PlayerGroup pg = new PlayerGroup();
        int lobbySize = users.size();
        //random number that decides who MrX is
        int whoIsMrX = ThreadLocalRandom.current().nextInt(0, lobbySize);
        int i = 0;
        for (User currUser: users){

            Player newPlayer = new Player();
            newPlayer.setUser(currUser);

            // PlayerClass gets assigned by Random number
            if(i == whoIsMrX){
                newPlayer.setPlayerClass(PlayerClass.MRX);
            } else {
                newPlayer.setPlayerClass(PlayerClass.AGENT);
            }
            pg.add(newPlayer);
            i++;
        }
        return pg;

    }


}
