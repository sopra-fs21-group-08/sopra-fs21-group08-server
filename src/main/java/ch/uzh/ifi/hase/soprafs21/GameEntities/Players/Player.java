package ch.uzh.ifi.hase.soprafs21.GameEntities.Players;



import ch.uzh.ifi.hase.soprafs21.GameEntities.TicketWallet.TicketWallet;
import ch.uzh.ifi.hase.soprafs21.constant.PlayerClass;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.network.Station;
import ch.uzh.ifi.hase.soprafs21.rest.PlayerDTO.PlayerGetDTO;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
@Table(name = "PLAYER")
public class Player {

    @Id
    private Long playerId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    @MapsId
    private User user;

    @OneToOne
    private Station currentStation;

    private PlayerClass playerClass;

    @Transient
    private TicketWallet wallet;

    public Station getCurrentStation() {
        return currentStation;
    }
    public void setCurrentStation(Station currentStation) {
        this.currentStation = currentStation;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerClass(PlayerClass playerClass) {
        this.playerClass = playerClass;
    }
    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public boolean isMrX(){
        return this.playerClass == PlayerClass.MRX;
    }
}
