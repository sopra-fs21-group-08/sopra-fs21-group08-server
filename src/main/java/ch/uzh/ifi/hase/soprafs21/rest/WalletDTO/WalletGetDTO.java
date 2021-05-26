package ch.uzh.ifi.hase.soprafs21.rest.WalletDTO;

public class WalletGetDTO {
    private int TRAIN;
    private int TRAM;
    private int BUS;
    private int BLACK;
    private int DOUBLE;


    public void setTRAIN(int TRAIN) {
        this.TRAIN = TRAIN;
    }

    public void setTRAM(int TRAM) {
        this.TRAM = TRAM;
    }

    public void setBUS(int BUS) {
        this.BUS = BUS;
    }

    public void setBLACK(int BLACK) {
        this.BLACK = BLACK;
    }

    public void setDOUBLE(int DOUBLE) {
        this.DOUBLE = DOUBLE;
    }
}
