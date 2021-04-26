package ch.uzh.ifi.hase.soprafs21.rest.MrXDTO;

import ch.uzh.ifi.hase.soprafs21.rest.WalletDTO.SpecialWalletGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.WalletDTO.WalletGetDTO;

public class MrXGetDTO {
    private String username;
    private Long userId;
    private Long stationId;
    private WalletGetDTO wallet;
    private SpecialWalletGetDTO specialWallet;

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

    public WalletGetDTO getWallet() {
        return wallet;
    }

    public void setWallet(WalletGetDTO wallet) {
        this.wallet = wallet;
    }

    public SpecialWalletGetDTO getSpecialWallet() {
        return specialWallet;
    }

    public void setSpecialWallet(SpecialWalletGetDTO specialWallet) {
        this.specialWallet = specialWallet;
    }
}
