package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.GameEntities.GameSummary;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Blackboard;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Move;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Round;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.RoundHistory;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.PlayerGroup;
import ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet.Ticket;
import ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet.TicketWallet;
import ch.uzh.ifi.hase.soprafs21.constant.PlayerClass;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.network.Station;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.GameSummaryRepository;
import ch.uzh.ifi.hase.soprafs21.repository.StationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    private final StationRepository stationRepository;
    private final GameSummaryRepository gameSummaryRepository;

    private Random rand = new Random();

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository,
                       @Qualifier("stationRepository") StationRepository stationRepository,
                       @Qualifier("gameSummaryRepository") GameSummaryRepository gameSummaryRepository) {
        this.gameRepository = gameRepository;
        this.stationRepository = stationRepository;
        this.gameSummaryRepository = gameSummaryRepository;
    }


    public void isUserInGame(long gameId, User foundUser){
        Game foundGame = gameRepository.findByGameId(gameId);
        if (Objects.isNull(foundGame)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The game was not found");
        }
        Player foundPlayer = foundGame.getPlayerGroup().findCorrespondingPlayer(foundUser);
        if (Objects.isNull(foundPlayer)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User is not in this game");
        }
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
        theGame.successfulTurn();

        //check if next player can move
        canCurrentPlayerMove(gameId);

        gameRepository.flush();

        return currentPlayer;
    }

    public Game initializeGame(Lobby lobby){

        Game game = new Game();
        game.setGameId(lobby.getLobbyId());

        //transforming user list into a player list
        PlayerGroup pg = createPlayerGroup(lobby.getUsers());
        pg.setGame(game);

        // initializing the first round
        Round newRound = new Round();
        newRound.setRoundNumber(1);
        newRound.setMaxMoves(lobby.getSize());

        // initializing the Blackboard
        Blackboard blackboard = new Blackboard();

        // init RoundHistory
        RoundHistory rh = new RoundHistory();

        // giving the game all its support entities
        game.setPlayerGroup(pg);
        game.setCurrentRound(newRound);
        game.setBlackboard(blackboard);
        game.setRoundHistory(rh);


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

    public List<Player> getPlayerPositions(long gameId, User issuingUser){
        List<Player> playerList = new ArrayList<>();
        Game currentGame = getGameByGameId(gameId);
        PlayerGroup playerGroup = getPlayerGroupByGameId(gameId);

        Player mrxReal = playerGroup.getMrX();

        //find the player that issues the request
        Player issuingPlayer = playerGroup.findCorrespondingPlayer(issuingUser);

        //depending on issuingPlayers PlayerClass it will change MRX current Station
        if(issuingPlayer.isMrX()){
            playerList.add(mrxReal);
        } else {

            //filling up the requirements for DTO
            Player mrxDouble = new Player();

            mrxDouble.setUser(mrxReal.getUser());
            mrxDouble.setWallet(mrxReal.getWallet());
            mrxDouble.setPlayerClass(mrxReal.getPlayerClass());

            //last visible position or null
            if(currentGame.getCurrentRound().isMrXVisible()){
                mrxDouble.setCurrentStation(mrxReal.getCurrentStation());
            } else {
                mrxDouble.setCurrentStation(currentGame.getRoundHistory().getLastVisibleStation());
            }

            //add MrX to the list
            playerList.add(mrxDouble);
        }

        //fills up the list with players
        for(Player player: playerGroup){
            if(player.isMrX()){
                continue;
            }
            playerList.add(player);
        }




        return playerList;
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
     * give random stations for game init
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

    /**
     * Checks if
     * @param guy
     * @param game
     * @return
     */
    private Player isUsersTurn(User guy, Game game) {
        String baseErrorMessage = "It is not your Turn!";
        if (!guy.getUserId().equals(game.getCurrentPlayer().getPlayerId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, baseErrorMessage);
        }
        return game.getCurrentPlayer();
    }

    // no implementation of special ticket
    public void isMovePossible(Move move) {

        String baseErrorMessage = "Move isn't possible, Sorry";
        if (move.getTicket() == Ticket.DOUBLE) {
            if (!possibleStationsDoubleTicket(move.getPlayer())
                    .contains(move.getTo().getStationId())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, baseErrorMessage);
            }
        } else {
            if (!move.getFrom().get_reachable_by_ticket(move.getTicket())
                    .contains(move.getTo().getStationId())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, baseErrorMessage);
            }
        }
    }

    public List<Station> possibleStations(Game game, User user, Ticket ticket){

        List<Station> possibleStationList = new ArrayList<>();

        Player foundPlayer = game.findCorrespondingPlayer(user);
        if (foundPlayer.getWallet().isTicketAvailable(ticket)){

            List<Long> possibleStationIdList;

            if (ticket == Ticket.DOUBLE){
                possibleStationIdList = possibleStationsDoubleTicket(foundPlayer);
            } else {
                possibleStationIdList = foundPlayer.getCurrentStation().get_reachable_by_ticket(ticket);
                }
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

    private List<Long> possibleStationsDoubleTicket(Player player){

        List<Long> possibleStationIdList = new ArrayList<>();
        Set<Long> possibleStationIdSet = new HashSet<>();

        Set<Long> intermediateStationIdList = player.getCurrentStation().get_reachable_distinct();

        for (Long stationId : intermediateStationIdList){
            Station foundStation = stationRepository.findByStationId(stationId);
            possibleStationIdSet.addAll(foundStation.get_reachable_distinct());
        }

        possibleStationIdList.addAll(possibleStationIdSet);

        return possibleStationIdList;
    }

    /**
     * This is the hack function we will call from the hack-endpoint to show a smooth game
     * @param gameId
     */
    public void hack(long gameId) {

        Game hackGame = gameRepository.findByGameId(gameId);
        ArrayList<Long> stationList = new ArrayList<>();

        stationList.add(232L);
        stationList.add(194L);
        stationList.add(16L);
        stationList.add(188L);
        stationList.add(288L);
        stationList.add(206L);
        int i = 0;


        for (Player player : hackGame.getPlayerGroup()){
            player.setCurrentStation(stationRepository.findByStationId(stationList.get(i)));
            i++;
        }


    }

    public Game getGameStatusById(long gameId) {

        Game game = this.gameRepository.findByGameId(gameId);

        if(game.isGameOver()){
            GameSummary gs = game.getGameSummary();
            // Do game summary aftermath

        }
        return game;
    }

    public GameSummary getGameSummary(long gameId) {
        return this.gameSummaryRepository.findBySummaryId(gameId);
    }

    public Move createValidMove(Game createdGame, User currUser){

        Move testMove = new Move();

        // get a possible station to move to
        List<Station> possibleTramStations = possibleStations(createdGame, currUser, Ticket.TRAM);
        List<Station> possibleBusStations = possibleStations(createdGame, currUser, Ticket.BUS);
        List<Station> possibleTrainStations = possibleStations(createdGame, currUser, Ticket.TRAIN);
        if (!possibleTramStations.isEmpty()){
            testMove.setTicket(Ticket.TRAM);
            testMove.setTo(possibleTramStations.get(0));
        } else if(!possibleBusStations.isEmpty()){
            testMove.setTicket(Ticket.BUS);
            testMove.setTo(possibleBusStations.get(0));
        } else if (!possibleTrainStations.isEmpty()){
            testMove.setTicket(Ticket.TRAIN);
            testMove.setTo(possibleTrainStations.get(0));
        } else{
            throw new IllegalStateException("Something went terribly wrong while creating a valid move!");
        }

        return testMove;
    }

    public void canCurrentPlayerMove(long gameId) {

        Game game = gameRepository.findByGameId(gameId);

        TicketWallet currentPlayerWallet = game.getCurrentPlayer().getWallet();
        Station currentStation = game.getCurrentPlayer().getCurrentStation();

        boolean canHeMove = false;

        if(!currentStation.get_reachable_by_bus().isEmpty()){
            if(currentPlayerWallet.isTicketAvailable(Ticket.BUS)){
                canHeMove = true;
            }
        }

        if(!currentStation.get_reachable_by_train().isEmpty()){
            if(currentPlayerWallet.isTicketAvailable(Ticket.TRAIN)){
                canHeMove = true;
            }
        }

        if(!currentStation.get_reachable_by_tram().isEmpty()){
            if(currentPlayerWallet.isTicketAvailable(Ticket.TRAM)){
                canHeMove = true;
            }
        }

        if(currentPlayerWallet.isTicketAvailable(Ticket.BLACK) || currentPlayerWallet.isTicketAvailable(Ticket.DOUBLE)){
            canHeMove = true;
        }

        if(!canHeMove){
            Move emptyMove = new Move();
            game.getCurrentRound().addMove(emptyMove);
            emptyMove.setRound(game.getCurrentRound());
            game.successfulTurn();
        }


    }
}
