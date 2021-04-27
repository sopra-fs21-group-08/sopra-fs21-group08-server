package ch.uzh.ifi.hase.soprafs21.GameEntities.Movement;


import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ROUND")
public class Round implements Iterable<Move>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roundId;

    @OneToOne(mappedBy = "currentRound")
    private Game game;

    @OneToMany
    private List<Move> moves = new ArrayList();

    private int roundNumber;

    //information to determine if the round is over
    private boolean isRoundOver = false;
    private int maxMoves;

    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }

    protected List<Move> getMoves() {
        return moves;
    }
    protected void setMoves(List<Move> moves) {
        this.moves = moves;
    }
    protected void addMove(Move move){
        this.moves.add(move);
        if (maxMoves==getSize()){
            setRoundOver(true);
        }
    }  //will set isRoundOver to true automatically if its over
    public int getSize() {
        return moves.size();
    }

    public void setRoundId(Long id) {
        this.roundId = id;
    }
    public Long getRoundId() {
        return roundId;
    }

    public int getRoundNumber() {
        return roundNumber;
    }
    public void setRoundNumber(int i) {
        this.roundNumber = i;
    } //sets the number of the round in the current game
    public int incrementRoundNumber(){
        return roundNumber+1;
    }

    public boolean isRoundOver() {
        return isRoundOver;
    }
    public void setRoundOver(boolean roundOver) {
        isRoundOver = roundOver;
    }

    public int getMaxMoves() {
        return maxMoves;
    }
    public void setMaxMoves(int maxMoves) {
        this.maxMoves = maxMoves;
    }

    @Override
    public RoundIterator iterator() {
        return new RoundIterator(this);
    }

};
