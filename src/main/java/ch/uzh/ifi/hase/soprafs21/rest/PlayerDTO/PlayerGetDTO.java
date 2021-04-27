package ch.uzh.ifi.hase.soprafs21.rest.PlayerDTO;

import ch.uzh.ifi.hase.soprafs21.constant.PlayerClass;
import ch.uzh.ifi.hase.soprafs21.rest.UserDTO.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.WalletDTO.WalletGetDTO;

public class PlayerGetDTO {
    private UserGetDTO user;
    private Long playerId;
    private PlayerClass playerClass;
    private WalletGetDTO wallet;

    public WalletGetDTO getWallet() {
        return wallet;
    }
    public void setWallet(WalletGetDTO wallet) {
        this.wallet = wallet;
    }

    public UserGetDTO getUser() {
        return user;
    }
    public void setUser(UserGetDTO user) {
        this.user = user;
    }

    public Long getPlayerId() {
        return playerId;
    }
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }
    public void setPlayerClass(PlayerClass playerClass) {
        this.playerClass = playerClass;
    }

}
