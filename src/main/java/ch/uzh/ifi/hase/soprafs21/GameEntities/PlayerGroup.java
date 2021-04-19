package ch.uzh.ifi.hase.soprafs21.GameEntities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PLAYERGROUP")
public class PlayerGroup {

    @Id
    private Long playerGroupId = this.game.getGameId();

    @OneToOne
    private Game game;

    @OneToMany
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
}
