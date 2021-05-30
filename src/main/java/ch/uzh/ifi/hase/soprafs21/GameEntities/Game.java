package ch.uzh.ifi.hase.soprafs21.GameEntities;


import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Blackboard;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Move;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Round;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.RoundHistory;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.PlayerGroup;
import ch.uzh.ifi.hase.soprafs21.constant.PlayerClass;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.network.Station;

import javax.persistence.*;
import java.util.List;

/**
 * The <code>Game</code> class is the main nervous system of the Game, it holds all helper classes and manipulates them according to needs.
 * @author Filip Dombos
 */
@Entity
@Table(name= "GAME")
public class Game {

    @Id
    private Long gameId;

    // holds all participating players
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private PlayerGroup playerGroup;

    // keeps track of the current Round
    @OneToOne(cascade = CascadeType.ALL)
    private Round currentRound;

    // keeps track of the tickets used by MrX
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Blackboard myBlackboard;

    // the maximal number of rounds in each game
    private final int roundsPerGame = 22;

    // keeps track of all past rounds, in case some information from past rounds is required
    @OneToOne(cascade = CascadeType.ALL)
    private RoundHistory roundHistory;

    // after a game is over, the game gets "memorialized" into a GameSummary.
    // This new class stores are relevant information about the class for representation.
    // This allows us to delete the game and only keep the summary.
    @OneToOne(cascade = CascadeType.PERSIST)
    private GameSummary gameSummary;

    private boolean isGameOver = false;

    /**
     * Uses the <code>User</code> to find the corresponding <code>Player</code>
     * @param user of which you want to find the <code>Player</code>
     * @return the <code>Player</code> which the <code>User</code> controlls
     */
    public Player findCorrespondingPlayer(User user){
        return playerGroup.findCorrespondingPlayer(user);
    }
    public PlayerGroup getPlayerGroup() {
        return playerGroup;
    }
    public void setPlayerGroup(PlayerGroup playerGroup) {
        this.playerGroup = playerGroup;
    }

    public Long getGameId() {
        return gameId;
    }
    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Round getCurrentRound() {
        return currentRound;
    }
    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }

    /**
     * Simple renamed Getter
     * @return boolean showing if the <code>Game</code> is over or not.
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * When gameOver is called the games isGameOver Boolean is set to true.
     * @param winner is the PlayerClass who won the game and is used to create the GameSummary.
     */
    public void gameOver (PlayerClass winner) {
        createGameSummary(winner);
        playerGroup.gameOverCredits(winner);
        isGameOver = true;
    }

    public void abortGame(){
        isGameOver = true;
    }

    /**
     * Memorializes the <code>Game</code> class with a <code>GameSummary</code> class.
     * @param winner is the PlayerClass who won the game and is used to create the GameSummary.
     */
    private void createGameSummary(PlayerClass winner) {
        GameSummary summary = new GameSummary();
        summary.setSummaryId(this.getGameId());

        summary.setWinner(winner);
        summary.setRoundsPlayed(this.currentRound.getRoundNumber());

        this.gameSummary = summary;
    }
    public GameSummary getGameSummary() {
        return gameSummary;
    }

    public Blackboard getBlackboard() {
        return myBlackboard;
    }
    public void setBlackboard(Blackboard blackboard) {
        this.myBlackboard = blackboard;
    }

    /**
     * @return <code>Player</code> whos turn it is
     */
    public Player getCurrentPlayer(){
        return this.playerGroup.getCurrentPlayer();
    }

    /**
     * Personalizes a move for the <code>currentPlayer</code>
     * @param move takes a move and updates it for the <code>currentPlayer</code>.
     * @return updated move with <code>currentPlayer</code>'s information
     */
    public Move createMoveForCurrentPlayer(Move move){
        move = getCurrentPlayer().setPlayersLocationAndUseTicket(move);
        this.getCurrentRound().addMove(move);
        move.setRound(currentRound);
        return move;
    }

    /**
     * @return the <code>Player</code> who is MrX
     */
    public Player getMrX(){
        return this.playerGroup.getMrX();
    }

    public RoundHistory getRoundHistory() {
        return roundHistory;
    }
    public void setRoundHistory(RoundHistory roundHistory) {
        this.roundHistory = roundHistory;
    }

    /**
     * A function that keeps the gears of the game turning, only call this function and it takes care of, turns
     * the rounds and the win conditions.
     */
    public void successfulTurn(){
        if (!checkWinCondition()) {
            this.playerGroup.incrementPlayerTurn();
            if(currentRound.isRoundOver()){
                successfulRound();
            }
        }
    }

    /**
     * Checks if a player has moved to MrX's current location or if MrX moved onto a Detectives Location.
     * WinCondition #1 --> Detectives Win, MrX position is infiltrated by a Detective.
     * @return a boolean if WinCondition #1 is met.
     */
    private boolean checkWinCondition() {
        List<Station> positions = this.playerGroup.getPlayerLocations();
        Station mrx = positions.get(0);

        for (int i=1; i<positions.size(); i++){
            if (mrx.getStationId().equals(positions.get(i).getStationId())){
                this.gameOver(PlayerClass.DETECTIVE);
                return true;
            }
        }
        return false;
    }

    /**
     * If the round is successfully finished this method will
     * WinCondition #2 --> MrX Wins, MrX escaped for <code>roundsPerGame</code> Rounds.
     */
    private void successfulRound(){
        if(currentRound.getRoundNumber() == roundsPerGame) {
            this.gameOver(PlayerClass.MRX);
        }
        myBlackboard.addTicket(currentRound.getMrXTicket());

        //create new round
        Round newRound = new Round();
        newRound.createNextRound(currentRound);

        //old round gets saved to history
        this.roundHistory.addRound(currentRound);

        setCurrentRound(newRound);
    }



}
