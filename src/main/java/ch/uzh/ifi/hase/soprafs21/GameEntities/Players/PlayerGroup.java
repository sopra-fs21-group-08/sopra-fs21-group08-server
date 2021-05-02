package ch.uzh.ifi.hase.soprafs21.GameEntities.Players;

import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.constant.PlayerClass;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.network.Station;

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

    // how many turns have been made by players, this will descide which players turn it is.
    private int playerTurn;

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


    public Player getCurrentPlayer() {
        int currInt =  playerTurn%getSize();
        return getPlayers().get(currInt);
    }
    public void incrementPlayerTurn(){
        this.playerTurn++;
    }
    public void resetPlayerTurn() {
        this.playerTurn = 0;
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

    public void moveMRXToTopOfList(){
        int i = 0;
        for (Player p: this){
            if(p.getPlayerClass()== PlayerClass.MRX){
                if(i==0){
                    break;
                }
                // saves first player to temp
                Player temp = this.getPlayers().get(0);
                // sets MRX to first player in list
                this.getPlayers().set(0, this.getPlayers().get(i));
                // sets temp player to MRX's position.
                this.getPlayers().set(i, temp);
            }
            i++;
        }
    }

    public Player getMrX(){
        return this.getPlayers().get(0);
    }

    @Override
    public PlayerGroupIterator iterator() {
        return new PlayerGroupIterator(this);
    }

    public List<Station> getPlayerLocations() {
        List<Station> list = new ArrayList<>();

        for(Player player : players){
            list.add(player.getCurrentStation());
        }
        return list;
    }
}
