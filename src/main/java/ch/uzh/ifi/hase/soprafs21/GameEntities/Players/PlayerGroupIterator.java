package ch.uzh.ifi.hase.soprafs21.GameEntities.Players;

import java.util.Iterator;

public class PlayerGroupIterator implements Iterator<Player> {

    private int i;
    private PlayerGroup players;

    public PlayerGroupIterator(PlayerGroup players){
        this.i = 0;
        this.players = players;
    }

    @Override
    public boolean hasNext() {
        return(i < players.getSize());
    }

    @Override
    public Player next() {
        Player player = players.getPlayers().get(i);
        i++;
        return player;
    }

    @Override
    public void remove() {
        //implement this later
    }
}
