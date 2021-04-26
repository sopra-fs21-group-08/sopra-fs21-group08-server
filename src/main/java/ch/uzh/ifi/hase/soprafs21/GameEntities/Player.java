package ch.uzh.ifi.hase.soprafs21.GameEntities;


import ch.uzh.ifi.hase.soprafs21.Helpers.TicketWallet.DetectiveTicketWallet;
import ch.uzh.ifi.hase.soprafs21.Helpers.TicketWallet.TicketWallet;
import ch.uzh.ifi.hase.soprafs21.constant.PlayerClass;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.network.Station;
import ch.uzh.ifi.hase.soprafs21.rest.PlayerDTO.PlayerGetDTO;

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

    @Transient
    private DetectiveTicketWallet wallet;

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

    public PlayerGetDTO convertToPlayerDTO(){
        PlayerGetDTO playerGetDTO = new PlayerGetDTO();
        playerGetDTO.setUserId(this.user.getUserId());
        playerGetDTO.setUsername(this.user.getUsername());
        playerGetDTO.setStationId(this.currentStation.getId());
        playerGetDTO.setWallet(this.wallet.convertToWalletDTO());
        return playerGetDTO;
    }
}
