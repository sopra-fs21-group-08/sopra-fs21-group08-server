package ch.uzh.ifi.hase.soprafs21.GameEntities.Movement;

import java.util.Iterator;
import java.util.NoSuchElementException;

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
        if(!hasNext()){
            throw new NoSuchElementException();
        }
        Move move = moves.getMoves().get(i);
        i++;
        return move;
    }

    @Override
    public void remove() {

    }
}
