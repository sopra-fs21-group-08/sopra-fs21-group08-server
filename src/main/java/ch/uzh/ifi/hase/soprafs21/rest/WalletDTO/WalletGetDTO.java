package ch.uzh.ifi.hase.soprafs21.rest.WalletDTO;

public class WalletGetDTO {
    private int train;
    private int tram;
    private int bus;



    public int getTrain() {
        return train;
    }
    public void setTrain(int train) {
        this.train = train;
    }

    public int getTram() {
        return tram;
    }
    public void setTram(int tram) {
        this.tram = tram;
    }

    public int getBus() {
        return bus;
    }
    public void setBus(int bus) {
        this.bus = bus;
    }
}
