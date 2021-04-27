package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Round;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.PlayerGroup;
import ch.uzh.ifi.hase.soprafs21.constant.PlayerClass;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.network.Station;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.StationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    private final StationRepository stationRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository,
                       @Qualifier("stationRepository") StationRepository stationRepository) {
        this.gameRepository = gameRepository;
        this.stationRepository = stationRepository;
    }


    public Game initializeGame(Lobby lobby){

        Game game = new Game();


        //transforming user list into a player list
        PlayerGroup pg = createPlayerGroup(lobby.getUsers());
        pg.setGame(game);
        pg.resetPlayerTurn();       // this is an int that will give determine which players turn it is.

        // initializing the first round
        Round newRound = new Round();
        newRound.setRoundNumber(1);
        newRound.setMaxMoves(lobby.getSize());


        // setting all the preparations to the game
        game.setPlayerGroup(pg);
        game.setCurrentRound(newRound);
        game.setLobby(lobby);


        game = gameRepository.save(game);
        gameRepository.flush();
        // the game gets saved to the lobby after this return
        return game;
    }


    public Game getGameByEntity(Game game) {
        return this.gameRepository.findByGameId(game.getGameId());
    }

    public Game getGameByGameId(long gameId) {
        return this.gameRepository.findByGameId(gameId);
    }

    public PlayerGroup getPlayerGroupByGameId(long gameId){
        return this.gameRepository.findByGameId(gameId).getPlayerGroup();
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
        // random stations to assign to the players
        List<Station> randomStations = getNRandomDifferentStations(lobbySize);
        for (User currUser: users){

            Player newPlayer = new Player();
            newPlayer.setUser(currUser);
            newPlayer.setCurrentStation(randomStations.get(i));
            // PlayerClass gets assigned by Random number
            if(i == whoIsMrX){
                newPlayer.setPlayerClass(PlayerClass.MRX);
            } else {
                newPlayer.setPlayerClass(PlayerClass.AGENT);
            }
            pg.add(newPlayer);
            i++;
        }

        //change order of pg so that MrX is always first in the list
        pg.moveMRXToTopOfList();
        return pg;

    }
    /**
     *
     * @param totalStations
     * @return a list of stations of size totalStations without repeats
     */
    private List<Station> getNRandomDifferentStations(int totalStations){
        Random rand = new Random();

        // create a temporary list for storing
        // selected element
        List<Station> randomStations = new ArrayList<>();
        List<Station> allStations = stationRepository.findAll();
        for (int i = 0; i < totalStations; i++) {

            // take a random index between 0 to size
            // of given List
            int randomIndex = rand.nextInt(allStations.size());

            // add element in temporary list
            randomStations.add(allStations.get(randomIndex));

            // Remove selected element from orginal list
            allStations.remove(randomIndex);
        }
        return randomStations;
    }


}
