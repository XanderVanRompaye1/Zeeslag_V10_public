package be.kdg.view.optiesView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
/*
 * Xander Van Rompaye
 * 2020-2021
 */
public class OptiesView extends BorderPane {
    private Label lblSpelopties;
    private Label lblNaam;
    private Label lblGrootteSpeelveld;
    private TextField tfNaam;
    private ComboBox<String> cboxGrootteSpeelveld;
    private Button btnStartSpel;
    private GridPane gridPane;
    private Image background;

    public OptiesView() {
        this.initialiseNodes();
        this.layoutNodes();
    }

    private void initialiseNodes() {
        lblSpelopties = new Label("SPELOPTIES");
        lblNaam = new Label("NAAM");
        lblGrootteSpeelveld = new Label("GROOTTE SPEELVELD");
        tfNaam = new TextField();
        btnStartSpel = new Button("START SPEL");
        gridPane = new GridPane();

        ObservableList<String> speelveldGrootte =
                FXCollections.observableArrayList("8X8", "10X10", "12X12");

        cboxGrootteSpeelveld = new ComboBox<>(speelveldGrootte);
        cboxGrootteSpeelveld.getSelectionModel().select(1);

        background = new Image("images/startBackground.jpg");
    }

    private void layoutNodes() {

        lblNaam.setFont(new Font("Arial", 25));
        lblSpelopties.setFont(new Font("Arial", 40));
        lblGrootteSpeelveld.setFont(new Font("Arial", 25));
        btnStartSpel.setFont(new Font("Arial", 30));
        setPadding(new Insets(10));
        gridPane.add(lblSpelopties, 1, 0);
        gridPane.add(lblNaam, 0, 1);
        gridPane.add(lblGrootteSpeelveld, 0, 2);
        gridPane.add(tfNaam, 2, 1);
        gridPane.add(cboxGrootteSpeelveld, 2, 2);
        gridPane.add(btnStartSpel, 1, 3);
        gridPane.setHgap(10);
        gridPane.setVgap(35);
        setPadding(new Insets(10));
        gridPane.setMaxWidth(850);
        gridPane.setMaxHeight(400);

        super.setCenter(gridPane);
        gridPane.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(btnStartSpel, Pos.CENTER);
        BorderPane.setAlignment(gridPane, Pos.CENTER);

        gridPane.setBackground(new Background(
                new BackgroundFill(Color.rgb(50, 50, 50, 0.7),
                        new CornerRadii(50),
                        new Insets(10))));


        this.setBackground(new Background(
                new BackgroundImage(background,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        new BackgroundPosition(Side.LEFT, 0.0, false, Side.BOTTOM, 0.0, false),
                        new BackgroundSize(500, 500, false, false, false, true)
                )));

    }

    ComboBox<String> getCboxGrootteSpeelveld() {
        return cboxGrootteSpeelveld;
    }

    TextField getTfNaam() {
        return tfNaam;
    }

    Button getBtnStartSpel() {
        return btnStartSpel;
    }
}
