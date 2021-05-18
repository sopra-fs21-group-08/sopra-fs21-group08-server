package ch.uzh.ifi.hase.soprafs21.GameEntities.Movement;


import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet.Ticket;
import ch.uzh.ifi.hase.soprafs21.network.Station;

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

    @OneToMany(cascade = CascadeType.ALL)
    private List<Move> moves = new ArrayList<>();

    private int roundNumber;
    private boolean isMrXVisible;

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
    public void addMove(Move move){
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

    private int getMaxMoves() {
        return maxMoves;
    }
    public void setMaxMoves(int maxMoves) {
        this.maxMoves = maxMoves;
    }

    public boolean isMrXVisible() {
        return isMrXVisible;
    }
    private void setMrXVisible() {
        isMrXVisible = ((roundNumber + 2) % 5 == 0);
    }

    public Ticket getMrXTicket(){
        return this.moves.get(0).getTicket();
    }


    public Round createNextRound(Round lastRound) {

        this.setRoundNumber(lastRound.incrementRoundNumber());
        this.setMaxMoves(lastRound.getMaxMoves());
        this.setMrXVisible();

        return this;
    }

    public Station getMrXStation(){
        return this.moves.get(0).getTo();
    }

    @Override
    public RoundIterator iterator() {
        return new RoundIterator(this);
    }

};
