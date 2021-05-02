package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Blackboard;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Move;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Round;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.PlayerGroup;
import ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet.Ticket;
import ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet.TicketWallet;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.security.SecureRandom;
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

    private Random rand = new Random();

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository,
                       @Qualifier("stationRepository") StationRepository stationRepository) {
        this.gameRepository = gameRepository;
        this.stationRepository = stationRepository;
    }

    public Player playerIssuesMove(User issuingUser, Move issuedMove, long gameId){

        Game theGame = gameRepository.findByGameId(gameId);

        // checks if its users turn
        Player currentPlayer = isUsersTurn(issuingUser, theGame); // because its his turn we get the player form game

        // move is updated with currentPlayer and which Round it belongs to
        Move finishedMove = theGame.createMoveForCurrentPlayer(issuedMove);

        // check if move is possible
        isMovePossible(finishedMove);

        //wrap up the Turn
        finishedMove.executeMove();
        theGame.successfullTurn();

        gameRepository.flush();

        return currentPlayer;
    }

    public Game initializeGame(Lobby lobby){

        Game game = new Game();

        //transforming user list into a player list
        PlayerGroup pg = createPlayerGroup(lobby.getUsers());
        pg.setGame(game);


        // initializing the first round
        Round newRound = new Round();
        newRound.setRoundNumber(1);
        newRound.setMaxMoves(lobby.getSize());

        // initializing the Blackboard
        Blackboard blackboard = new Blackboard();

        // setting all the preparations to the game
        game.setPlayerGroup(pg);
        game.setCurrentRound(newRound);
        game.setLobby(lobby);
        game.setBlackboard(blackboard);

        game = gameRepository.save(game);
        gameRepository.flush();
        // the game gets saved to the lobby after this return
        return game;
    }


    public Game getGameByGameId(long gameId) {
        return this.gameRepository.findByGameId(gameId);
    }

    public PlayerGroup getPlayerGroupByGameId(long gameId){
        return this.gameRepository.findByGameId(gameId).getPlayerGroup();
    }

    public Player getPlayerByGameUserEntities(Game game, User user){
        return game.findCorrespondingPlayer(user);
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
            // give player the User
            newPlayer.setUser(currUser);
            // give player the reandom station
            newPlayer.setCurrentStation(randomStations.get(i));
            // PlayerClass gets assigned by Random number
            if(i == whoIsMrX){
                // MRX
                newPlayer.setPlayerClass(PlayerClass.MRX);

                // MRX TicketWallet
                TicketWallet wallet = new TicketWallet();
                wallet.createMrXWallet();
                newPlayer.setWallet(wallet);
            } else {
                // DETECTIVE
                newPlayer.setPlayerClass(PlayerClass.DETECTIVE);

                // DETECTIVE TicketWallet
                TicketWallet wallet = new TicketWallet();
                wallet.createDetectiveWallet();
                newPlayer.setWallet(wallet);
            }
            pg.add(newPlayer);
            i++;
        }

        //change order of pg so that MrX is always first in the list
        pg.moveMRXToTopOfList();
        pg.resetPlayerTurn();       // this is an int that will give determine which players turn it is.
        return pg;

    }


    /**
     *
     * @param totalStations
     * @return a list of stations of size totalStations without repeats
     */
    private List<Station> getNRandomDifferentStations(int totalStations){


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

    private Player isUsersTurn(User guy, Game game) {
        String baseErrorMessage = "It is not your Turn! dick";
        if (!guy.getUserId().equals(game.getCurrentPlayer().getPlayerId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, baseErrorMessage);
        }
        return game.getCurrentPlayer();
    }

    // no implementation of special ticket
    public void isMovePossible(Move move){

        String baseErrorMessage = "Move isn't possible, Sorry";
        if (!move.getFrom().get_reachable_by_ticket(move.getTicket())
                .contains(move.getTo().getStationId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, baseErrorMessage);
        }
    }

    public List<Station> possibleStations(Game game, User user, Ticket ticket){

        List<Station> possibleStationList = new ArrayList<>();

        Player foundPlayer = game.findCorrespondingPlayer(user);
        if (foundPlayer.getWallet().isTicketAvaiable(ticket)){
            List<Long> possibleStationIdList = foundPlayer.getCurrentStation().get_reachable_by_ticket(ticket);
            for (Long l : possibleStationIdList){
                possibleStationList.add(stationRepository.findByStationId(l));
            }
        }
        else{
            String baseErrorMessage = "User doesn't have this ticket anymore";

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, baseErrorMessage);

        }

        return possibleStationList;
    }

    public List<Ticket> getBlackboard(long gameId) {
        return gameRepository.findByGameId(gameId).getBlackboard().getTickets();
    }

    /**
     * This is the hack function we will call from the hackendpoint to show a smooth game
     * @param gameId
     */
    public void hack(long gameId) {

        Game hackGame = gameRepository.findByGameId(gameId);
        ArrayList<Long> stationList = new ArrayList<>();

        stationList.add(115L);
        stationList.add(67L);
        stationList.add(237L);
        stationList.add(182L);
        stationList.add(117L);
        int i = 0;


        for (Player player : hackGame.getPlayerGroup()){
            player.setCurrentStation(stationRepository.findByStationId(stationList.get(i)));
            i++;
        }


    }
}
