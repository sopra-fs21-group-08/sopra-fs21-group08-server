package ch.uzh.ifi.hase.soprafs21.GameEntities;


import javax.persistence.*;



@Entity
@Table(name= "GAME")
public class Game {

    @Id
    private Long gameId;

    public Long getGameId() {
        return gameId;
    }
    public void setGameId(Long id) {
        this.gameId = id;
    }

    @OneToOne
    private PlayerGroup playerGroup;


    public void addToPlayerGroup(Player player){
        playerGroup.add(player);
    }




}
