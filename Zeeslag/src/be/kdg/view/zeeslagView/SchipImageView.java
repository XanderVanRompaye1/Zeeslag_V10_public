package be.kdg.view.zeeslagView;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/*
 * Xander Van Rompaye
 * 2020-2021
 */
public class SchipImageView extends ImageView {
    private String schipNaam;
    private int schipId;

    SchipImageView(Image image, String schipNaam, int id) {
        super(image);
        this.schipNaam = schipNaam;
        this.schipId = id;
    }

    String getSchipNaam() {
        return schipNaam;
    }

    int getSchipId() {
        return schipId;
    }
}
