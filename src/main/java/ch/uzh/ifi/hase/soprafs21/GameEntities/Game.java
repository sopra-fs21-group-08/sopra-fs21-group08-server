package ch.uzh.ifi.hase.soprafs21.GameEntities;


import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Blackboard;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Move;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Round;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.RoundHistory;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.PlayerGroup;
import ch.uzh.ifi.hase.soprafs21.constant.PlayerClass;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.network.Station;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name= "GAME")
public class Game {

    @Id
    private Long gameId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "lobbyId")
    @MapsId
    private Lobby lobby;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private PlayerGroup playerGroup;

    @OneToOne(cascade = CascadeType.ALL)
    private Round currentRound;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Blackboard myBlackboard;


    private boolean isGameOver = false;
    private final int turnsPerGame = 22;

    @OneToOne(cascade = CascadeType.ALL)
    private RoundHistory roundHistory;

    @OneToOne(cascade = CascadeType.PERSIST)
    private GameSummary gameSummary;

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

    public Round getCurrentRound() {
        return currentRound;
    }
    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
    public void gameOver (PlayerClass winner) {

        createGameSummary(winner);

        //TODO: add game over stuff like, no more rounds, no more nextPlayer etc.

        isGameOver = true;
    }

    public void createGameSummary(PlayerClass winner) {
        GameSummary summary = new GameSummary();
        summary.setSummaryId(this.gameId);

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

    public Player getCurrentPlayer(){
        return this.playerGroup.getCurrentPlayer();
    }

    public Move createMoveForCurrentPlayer(Move move){
        move = getCurrentPlayer().setPlayersLocationAndUseTicket(move);
        this.getCurrentRound().addMove(move);
        move.setRound(currentRound);
        return move;
    }

    public Player getMrX(){
        return this.playerGroup.getMrX();
    }

    public RoundHistory getRoundHistory() {
        return roundHistory;
    }
    public void setRoundHistory(RoundHistory roundHistory) {
        this.roundHistory = roundHistory;
    }

    public void successfulTurn(){
        if (!checkWinCondition()) {
        this.playerGroup.incrementPlayerTurn();
        if(currentRound.isRoundOver()){
            successfulRound();
        }}
    }


    /**
     * WinCondition #1 --> Detectives Win, MrX position is infiltrated by a Detective.
     */
    private boolean checkWinCondition() {
        List<Station> positions = this.playerGroup.getPlayerLocations();
        Station mrx = positions.get(0);

        //TODO: rework to use iterator of PlayerGroup instead of this approach.
        for (int i=1; i<positions.size(); i++){
            if (mrx.getStationId().equals(positions.get(i).getStationId())){
                this.gameOver(PlayerClass.DETECTIVE);
                return true;
            }
        }
        return false;
    }

    private void successfulRound(){
        if(currentRound.getRoundNumber() == turnsPerGame) {
            //WinCondition #2 --> MrX Wins, MrX escaped for 22 Rounds.
            this.gameOver(PlayerClass.MRX);
        }
        myBlackboard.addTicket(currentRound.getMrXTicket());

        Round newRound = new Round();
        newRound.createNextRound(currentRound);

        //old round gets saved to history
        this.roundHistory.addRound(currentRound);

        setCurrentRound(newRound);
    }



}
