package ch.uzh.ifi.hase.soprafs21.GameEntities;


import ch.uzh.ifi.hase.soprafs21.Helpers.TicketWallet.TicketWallet;
import ch.uzh.ifi.hase.soprafs21.constant.PlayerClass;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.network.Station;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
public class Player {

    @Id
    private Long playerId = this.user.getUserId();

    @OneToOne
    private User user;

    @OneToOne
    private Station currentStation;

    public Station getCurrentStation() {
        return currentStation;
    }

    public void setCurrentStation(Station currentStation) {
        this.currentStation = currentStation;
    }

    private PlayerClass playerClass;

    private TicketWallet ticketWallet;



    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
    public TicketWallet getTicketWallet() {
        return ticketWallet;
    }
    public void setTicketWallet(TicketWallet ticketWallet) {
        this.ticketWallet = ticketWallet;
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
