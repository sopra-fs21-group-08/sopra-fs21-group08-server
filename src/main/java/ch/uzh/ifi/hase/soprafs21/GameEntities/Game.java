package ch.uzh.ifi.hase.soprafs21.GameEntities;


import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Blackboard;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Move;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Round;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.PlayerGroup;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lobbyId")
    @MapsId
    private Lobby lobby;

    @OneToOne(cascade = CascadeType.ALL)
    private PlayerGroup playerGroup;

    @OneToOne(cascade = CascadeType.ALL)
    private Round currentRound;

    @OneToOne(cascade = CascadeType.ALL)
    private Blackboard myBlackboard;

    @Transient
    private Player oldMrX;

    private boolean isGameOver = false;
    private final int turnsPerGame = 20;



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
    public void gameOver (boolean gameOver) {

        // include concluding operations for games

        isGameOver = gameOver;
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

    public void successfullTurn(){
        checkWinCondition();
        this.playerGroup.incrementPlayerTurn();
        if(currentRound.isRoundOver()){
            successfullRound();
        }
    }

    private void checkWinCondition() {
        List<Station> positions = this.playerGroup.getPlayerLocations();
        Station mrx = positions.get(0);
        for (int i=1; i<positions.size(); i++){
            if (mrx.getStationId().equals(positions.get(i).getStationId())){
                this.gameOver(true);
            }
        }
    }

    private void successfullRound(){
        if(currentRound.getRoundNumber() == turnsPerGame) {
            this.gameOver(true);
        }
        myBlackboard.addTicket(currentRound.getMrXTicket());

        Round newRound = new Round();
        newRound.createNextRound(currentRound);
        //TODO: save old round

        setCurrentRound(newRound);


    }

}
