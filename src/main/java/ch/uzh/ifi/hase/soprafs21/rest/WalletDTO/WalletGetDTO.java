package ch.uzh.ifi.hase.soprafs21.rest.WalletDTO;

public class WalletGetDTO {
    private int TRAIN;
    private int TRAM;
    private int BUS;
    private int BLACK;
    private int DOUBLE;


    public int getTRAIN() {
        return TRAIN;
    }

    public void setTRAIN(int TRAIN) {
        this.TRAIN = TRAIN;
    }

    public int getTRAM() {
        return TRAM;
    }

    public void setTRAM(int TRAM) {
        this.TRAM = TRAM;
    }

    public int getBUS() {
        return BUS;
    }

    public void setBUS(int BUS) {
        this.BUS = BUS;
    }

    public int getBLACK() {
        return BLACK;
    }

    public void setBLACK(int BLACK) {
        this.BLACK = BLACK;
    }

    public int getDOUBLE() {
        return DOUBLE;
    }

    public void setDOUBLE(int DOUBLE) {
        this.DOUBLE = DOUBLE;
    }
}
