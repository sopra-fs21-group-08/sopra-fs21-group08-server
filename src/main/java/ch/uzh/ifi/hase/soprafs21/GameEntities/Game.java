package ch.uzh.ifi.hase.soprafs21.GameEntities;


import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Blackboard;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Move;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Movement.Round;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.PlayerGroup;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;

import javax.persistence.*;


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



    private boolean isGameOver = false;




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
        this.playerGroup.incrementPlayerTurn();
        if(currentRound.isRoundOver()){
            successfullRound();
        }
    }
    private void successfullRound(){
        myBlackboard.addTicket(currentRound.getMrXTicket());

        Round newRound = new Round();
        newRound.createNextRound(currentRound);
        //TODO: save old round

        setCurrentRound(newRound);


    }
}
