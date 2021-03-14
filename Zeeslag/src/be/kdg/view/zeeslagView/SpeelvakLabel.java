package be.kdg.view.zeeslagView;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
/*
 * Xander Van Rompaye
 * 2020-2021
 */
public class SpeelvakLabel extends Label {
    private String positie;
    private Color kleur;

    public Color getKleur() {
        return kleur;
    }

    public void setKleur(Color kleur) {
        this.kleur = kleur;
    }

    SpeelvakLabel(String positie) {
        this.positie = positie;
    }

    String getPositie() {
        return positie;
    }
}
