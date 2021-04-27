package ch.uzh.ifi.hase.soprafs21.GameEntities.Movement;

import ch.uzh.ifi.hase.soprafs21.GameEntities.Players.Player;

import java.util.Iterator;

public class RoundIterator implements Iterator<Move> {

    private int i;
    private Round moves;

    public RoundIterator(Round moves) {
        this.i = 0;
        this.moves = moves;

    }

    @Override
    public boolean hasNext() {
        return(i < moves.getSize());
    }

    @Override
    public Move next() {
        Move move = moves.getMoves().get(i);
        i++;
        return move;
    }

    @Override
    public void remove() {

    }
}
