package be.kdg.model;
/*
 * Xander Van Rompaye
 * 2020-2021
 */
public class Speler {
    private String naam;
    private boolean isCpu;

    public boolean isCpu() {
        return isCpu;
    }

    Speler(String naam, boolean isCpu) {
        this.naam = naam;
        this.isCpu = isCpu;
    }

    public String getNaam() {
        return naam;
    }
}
