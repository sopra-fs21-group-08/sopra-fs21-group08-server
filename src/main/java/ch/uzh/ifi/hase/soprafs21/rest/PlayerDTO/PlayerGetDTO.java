package ch.uzh.ifi.hase.soprafs21.rest.PlayerDTO;

import ch.uzh.ifi.hase.soprafs21.Helpers.TicketWallet.DetectiveTicketWallet;
import ch.uzh.ifi.hase.soprafs21.rest.WalletDTO.WalletGetDTO;

public class PlayerGetDTO {
    private String username;
    private Long userId;
    private Long stationId;
    private WalletGetDTO wallet;

    public WalletGetDTO getWallet() {
        return wallet;
    }

    public void setWallet(WalletGetDTO wallet) {
        this.wallet = wallet;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

}
