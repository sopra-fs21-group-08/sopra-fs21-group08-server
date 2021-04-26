package ch.uzh.ifi.hase.soprafs21.GameEntities;

import ch.uzh.ifi.hase.soprafs21.entity.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PLAYERGROUP")
public class PlayerGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerGroupId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gameId")
    private Game game;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Player> players = new ArrayList<>();

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
}
