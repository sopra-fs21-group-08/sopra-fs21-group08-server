package ch.uzh.ifi.hase.soprafs21.rest.PlayerDTO;

import ch.uzh.ifi.hase.soprafs21.constant.PlayerClass;
import ch.uzh.ifi.hase.soprafs21.rest.UserDTO.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.WalletDTO.WalletGetDTO;

public class PlayerGetDTO {
    private UserGetDTO user;
    private PlayerClass playerClass;
    private Long stationId;
    private WalletGetDTO wallet;

    public UserGetDTO getUser() {
        return user;
    }
    public void setUser(UserGetDTO user) {
        this.user = user;
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }
    public void setPlayerClass(PlayerClass playerClass) {
        this.playerClass = playerClass;
    }


    public WalletGetDTO getWallet() {
        return wallet;
    }
    public void setWallet(WalletGetDTO wallet) {
        this.wallet = wallet;
    }

    public Long getStationId() {
        return stationId;
    }
    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }
}
