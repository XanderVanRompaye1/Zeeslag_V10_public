package be.kdg.view.zeeslagView;

import be.kdg.model.Speelveld;
import be.kdg.model.StatusVak;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
/*
 * Xander Van Rompaye
 * 2020-2021
 */
public class ZeeslagView  extends GridPane {
    private Label lblSpeelveldSpeler;
    private Label lblSpeelveldCpu;
    private int grootteSpeelveld;
    private SpeelvakLabel[][] labelsSpeler;
    private SpeelvakLabel[][] labelsCpu;
    private Label inputCommentaar;
    private GridPane gridPaneSpeler;
    private GridPane gridPaneCpu;
    private Image background;
    private SchipImageView vliegdekschipImage;
    private SchipImageView[] torpedoImages = new SchipImageView[2];
    private SchipImageView[] slagschipImages = new SchipImageView[3];
    private SchipImageView[] patrouilleschipImages = new SchipImageView[4];
    private GridPane gridPaneSchepen;

    public ZeeslagView(int grootteSpeelveld) {
        this.grootteSpeelveld = grootteSpeelveld;
        this.initialiseNodes();
        this.layoutNodes();
    }

    private void initialiseNodes() {
        lblSpeelveldCpu = new Label("CPU");
        lblSpeelveldSpeler = new Label();
        gridPaneSpeler = new GridPane();
        gridPaneCpu = new GridPane();
        inputCommentaar = new Label();
        labelsSpeler = new SpeelvakLabel[grootteSpeelveld][grootteSpeelveld];
        labelsCpu = new SpeelvakLabel[grootteSpeelveld][grootteSpeelveld];
        background = new Image("images/seaBackground.jpg");

        vliegdekschipImage = new SchipImageView(new Image("images/vliegdekschip6.png"), "vliegdekschip", 1);
        vliegdekschipImage.setFitHeight(70);
        vliegdekschipImage.setFitWidth(280);
        for (int i = 0; i < 2; i++) {
            torpedoImages[i] = new SchipImageView(new Image("images/torpedo4.png"), "torpedo", i + 1);
            torpedoImages[i].setFitWidth(170);
            torpedoImages[i].setFitHeight(60);
        }
        for (int i = 0; i < 3; i++) {
            slagschipImages[i] = new SchipImageView(new Image("images/slagschip3.png"), "slagschip", i + 1);
            slagschipImages[i].setFitWidth(120);
            slagschipImages[i].setFitHeight(50);
        }
        for (int i = 0; i < 4; i++) {
            patrouilleschipImages[i] = new SchipImageView(new Image("images/patrouilleschip2.png"), "patrouilleschip", i + 1);
            patrouilleschipImages[i].setFitHeight(40);
            patrouilleschipImages[i].setFitWidth(90);
        }
    }

    private void layoutNodes() {
        // GridPane van de speler opbouwen
        // Alle vakjes initialiseren en instellen
        for (int i = 0; i < labelsSpeler.length; i++) {
            for (int j = 0; j < labelsSpeler[i].length; j++) {
                labelsSpeler[i][j] = new SpeelvakLabel((j + 1) + String.valueOf((char) (i + 'A')));
                labelsSpeler[i][j].setMinWidth(40);
                gridPaneSpeler.add(labelsSpeler[i][j], i, j);
                labelsSpeler[i][j].setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-background-color: lightblue");
            }
        }

        // GridPane van de CPU opbouwen
        // Alle vakjes initialiseren en instellen
        for (int i = 0; i < labelsCpu.length; i++) {
            for (int j = 0; j < labelsCpu[i].length; j++) {
                labelsCpu[i][j] = new SpeelvakLabel((j + 1) + String.valueOf((char) (i + 'A')));
                labelsCpu[i][j].setMinWidth(40);
                gridPaneCpu.add(labelsCpu[i][j], i, j);
                labelsCpu[i][j].setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-background-color: lightblue");
            }
        }

        gridPaneSchepen = new GridPane();
        gridPaneSchepen.addRow(0, vliegdekschipImage);
        gridPaneSchepen.addRow(1, torpedoImages);
        gridPaneSchepen.addRow(2, slagschipImages);
        gridPaneSchepen.addRow(3, patrouilleschipImages);

        inputCommentaar.setText("Inputcommentaar");
        inputCommentaar.setAlignment(Pos.BOTTOM_CENTER);
        super.add(gridPaneSpeler, 1, 0);
        super.add(gridPaneCpu, 3, 0);
        super.add(gridPaneSchepen, 1, 1);
        super.add(inputCommentaar, 3, 2);
        super.add(lblSpeelveldSpeler, 0, 0);
        super.add(lblSpeelveldCpu, 2, 0);
        super.setHgap(10);

        this.setBackground(new Background(
                new BackgroundImage(background,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        new BackgroundPosition(Side.LEFT, 0.0, false, Side.BOTTOM, 0.0, false),
                        new BackgroundSize(500, 500, false, false, false, true)
                )));
    }

    void updateLabelsStatus(Speelveld speelveld) {
        SpeelvakLabel[][] labels = speelveld.getSpeler().isCpu() ? labelsCpu : labelsSpeler;

        for (SpeelvakLabel[] speelvakLabels : labels) {
            for (SpeelvakLabel label : speelvakLabels) {

                if (!speelveld.getSpeler().isCpu()) {
                    if (speelveld.getStatusVak(label.getPositie()).equals(StatusVak.leeg))
                        label.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

                    if (speelveld.getStatusVak(label.getPositie()) == StatusVak.gevuld)
                        label.setBackground(new Background(new BackgroundFill(speelveld.getSchipKleur(label.getPositie()), CornerRadii.EMPTY, Insets.EMPTY)));
                }

                if (speelveld.getStatusVak(label.getPositie()) == StatusVak.mis)
                    label.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                if (speelveld.getStatusVak(label.getPositie()) == StatusVak.raak)
                    label.setBackground(new Background(new BackgroundFill(Color.rgb(64, 64, 64), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
    }

    SpeelvakLabel[][] getLabelsSpeler() {
        return labelsSpeler;
    }

    SpeelvakLabel[][] getLabelsCpu() {
        return labelsCpu;
    }

    void setGrootteSpeelveld(int grootteSpeelveld) {
        this.grootteSpeelveld = grootteSpeelveld;
    }

    Label getInputCommentaar() {
        return inputCommentaar;
    }

    // TODO Alle schepen in 1 SchipImageView[] lijst bij houden
    SchipImageView getVliegdekschipImage() {
        return vliegdekschipImage;
    }

    SchipImageView[] getTorpedoImages() {
        return torpedoImages;
    }

    SchipImageView[] getSlagschipImages() {
        return slagschipImages;
    }

    SchipImageView[] getPatrouilleschipImages() {
        return patrouilleschipImages;
    }

    GridPane getGridPaneSpeler() {
        return gridPaneSpeler;
    }

    Label getLblSpeelveldSpeler() {
        return lblSpeelveldSpeler;
    }

    void toonSchip(String schipNaam, int id) {
        if (vliegdekschipImage.getSchipNaam().equals(schipNaam) && vliegdekschipImage.getSchipId() == id)
            vliegdekschipImage.setVisible(true);

        for (SchipImageView schipImageView : torpedoImages) {
            if (schipImageView.getSchipNaam().equals(schipNaam) && schipImageView.getSchipId() == id)
                schipImageView.setVisible(true);
        }

        for (SchipImageView schipImageView : slagschipImages) {
            if (schipImageView.getSchipNaam().equals(schipNaam) && schipImageView.getSchipId() == id)
                schipImageView.setVisible(true);
        }

        for (SchipImageView schipImageView : patrouilleschipImages) {
            if (schipImageView.getSchipNaam().equals(schipNaam) && schipImageView.getSchipId() == id)
                schipImageView.setVisible(true);
        }
    }

    boolean heeftSpelerAlleSchepenGeplaatst() {
        if (vliegdekschipImage.isVisible())
            return false;

        for (SchipImageView schipImageView : torpedoImages) {
            if (schipImageView.isVisible())
                return false;
        }

        for (SchipImageView schipImageView : slagschipImages) {
            if (schipImageView.isVisible())
                return false;
        }

        for (SchipImageView schipImageView : patrouilleschipImages) {
            if (schipImageView.isVisible())
                return false;
        }

        return true;
    }
}
