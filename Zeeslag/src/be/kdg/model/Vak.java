package be.kdg.model;
/*
 * Xander Van Rompaye
 * 2020-2021
 */
public class Vak {
    // Een vak heeft steeds een positie
    Vak(String positie) {
        this.positie = positie;
        status = StatusVak.leeg;
    }

    private String positie;
    private StatusVak status;
    private boolean hasHorizontalShip = true;

    private Schip schip;

    // Voorbeeld positie = 1A, waarbij 1 de rij aangeeft en A de kolom
    String getPositie() {
        return positie;
    }

    StatusVak getStatus() {
        return status;
    }

    boolean hasHorizontalShip() {
        return hasHorizontalShip;
    }

    Schip getSchip() {
        return schip;
    }

    void setSchip(Schip schip, boolean isHorizontal) {
        this.schip = schip;
        this.status = StatusVak.gevuld;
        this.hasHorizontalShip = isHorizontal;
    }

    void removeSchip() {
        this.schip = null;
        this.status = StatusVak.leeg;
        this.hasHorizontalShip = true;
    }

    int getRij() {
        return getRijVanPositie(this.positie);
    }

    int getKolom() {
        return getKolomVanPositie(this.positie);
    }

    boolean valAan() {
        if (status == StatusVak.raak || status == StatusVak.mis)
            return false;

        if (status == StatusVak.gevuld) {
            status = StatusVak.raak;
            return true;
        }

        status = StatusVak.mis;
        return false;
    }

    // Geeft de rij terug van een positie (minimum 1, maximum de grootte van het speelveld)
    // bv. speelveld van 10x10, minimum rij  = 1 en maximum rij = 10
    static int getRijVanPositie(String positie) {
        if (positie.length() == 3)
            return Integer.parseInt(positie.substring(0, 2));

        if (positie.length() == 2)
            return positie.charAt(0) - 48;

        throw new ZeeslagException(positie + " is geen geldige positie");
    }

    // Geeft de kolom terug van een positie (minimum 1, maximum de grootte van het speelveld)
    // bv. speelveld van 10x10, minimum kolom  = 1 en maximum kolom = 10
    static int getKolomVanPositie(String positie) {
        return positie.charAt(positie.length() - 1) - 64;
    }
}
