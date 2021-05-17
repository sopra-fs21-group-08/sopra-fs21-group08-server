package ch.uzh.ifi.hase.soprafs21.GameEntities.Movement;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RoundHistoryIterator implements Iterator<Round> {

    private int i;
    private final RoundHistory roundHistory;

    public RoundHistoryIterator(RoundHistory roundHistory) {
        this.i = roundHistory.size()-1;
        this.roundHistory = roundHistory;

    }

    @Override
    public boolean hasNext() {
        return(i < 0);
    }

    @Override
    public Round next() {
        if(!hasNext()){
            throw new NoSuchElementException();
        }
        Round round = roundHistory.getRounds().get(i);
        i--;
        return round;
    }

    @Override
    public void remove() {

    }
}