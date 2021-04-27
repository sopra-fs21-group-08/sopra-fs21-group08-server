package ch.uzh.ifi.hase.soprafs21.GameEntities.Players;

import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.entity.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

@Entity
@Table(name = "PLAYERGROUP")
public class PlayerGroup implements Iterable<Player>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerGroupId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gameId")
    private Game game;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Player> players = new ArrayList<>();

    protected List<Player> getPlayers() {
        return players;
    }
    protected void setPlayers(List<Player> players) {
        this.players = players;
    }
    protected int getSize(){return players.size();};

    public void setPlayerGroupId(Long id) {
        this.playerGroupId = id;
    }
    public Long getPlayerGroupId() {
        return playerGroupId;
    }

    public void add(Player player) {
        this.players.add(player);
    }

    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }


    //not sure if needed
    public Player getPlayerById(long id){
        Player playerToReturn = null;
        for(Player player : players) {
            if (player.getPlayerId() == id){
                playerToReturn = player;
            }
        }
        return playerToReturn;
    }

    public Player findCorrespondingPlayer(User user){
        for(Player player : players) {
            if (player.getUser() == user){
                return player;
            }
        }
        return null;
    }

    @Override
    public Iterator iterator() {
        return new PlayerGroupIterator(this);
    }

    @Override
    public void forEach(Consumer action) {

    }
}
