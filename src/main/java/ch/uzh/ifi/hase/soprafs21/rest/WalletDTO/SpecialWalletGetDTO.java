package ch.uzh.ifi.hase.soprafs21.rest.WalletDTO;

public class SpecialWalletGetDTO {
    private int doubleTicket;
    private int blackTicket;

    public int getDoubleTicket() {
        return doubleTicket;
    }

    public void setDoubleTicket(int doubleTicket) {
        this.doubleTicket = doubleTicket;
    }

    public int getBlackTicket() {
        return blackTicket;
    }

    public void setBlackTicket(int blackTicket) {
        this.blackTicket = blackTicket;
    }
}
