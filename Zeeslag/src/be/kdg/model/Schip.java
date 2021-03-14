package be.kdg.model;

import javafx.scene.paint.Color;
import java.util.Random;
/*
 * Xander Van Rompaye
 * 2020-2021
 */
public class Schip {
    private int lengte;
    private int id;
    private String naam;
    private Color kleur;

    Schip(String naam, int id, int lengte) {
        this.naam = naam;
        this.id = id;
        this.lengte = lengte;
        this.kleur = generateRandomColor();
    }

    public String getNaam() {
        return naam;
    }

    // TODO remove this method and create getSchipId = naam-id
    public int getId() {
        return id;
    }

    int getLengte() {
        return lengte;
    }

    Color getKleur() {
        return kleur;
    }

    private Color generateRandomColor() {
        Random random = new Random();
        var r = random.nextInt(255);
        var g = random.nextInt(255);
        var b = random.nextInt(160);
        return Color.rgb(r, g, b);
    }
}
