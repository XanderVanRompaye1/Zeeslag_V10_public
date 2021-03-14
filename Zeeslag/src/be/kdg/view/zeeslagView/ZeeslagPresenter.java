package be.kdg.view.zeeslagView;

import be.kdg.model.*;
import be.kdg.view.optiesView.OptiesPresenter;
import be.kdg.view.optiesView.OptiesView;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
/*
 * Xander Van Rompaye
 * 2020-2021
 */
public class ZeeslagPresenter {
    private Zeeslag model;
    private ZeeslagView view;

    public ZeeslagPresenter(Zeeslag model, ZeeslagView view) {
        view.getInputCommentaar().setText(String.format("Sleep uw schepen op het bord%nklik op 1e positie van schip = schip draaien%nshift + linkermuisklik = schip verwijderen").toUpperCase());
        view.getLblSpeelveldSpeler().setText(model.getSpeler().getNaam());
        this.model = model;
        this.view = view;
        this.addEventHandlers();
        this.initialiseerSpel();
    }

    private void addEventHandlers() {

        // Voeg onDragDetected Eventhandler toe aan de source (imageviews van schepen)
        EventHandler<MouseEvent> dragStarted = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ImageView source = (ImageView) event.getSource();
                // Image wordt in het DragBoard gestopt tijdens de transfer
                Dragboard dragboard = source.startDragAndDrop(TransferMode.COPY);
                ClipboardContent content = new ClipboardContent();
                content.putImage(source.getImage());
                dragboard.setContent(content);
                event.consume();
            }
        };

        // Maak alle Images draggable
        view.getVliegdekschipImage().setOnDragDetected(dragStarted);

        for (int i = 0; i < 2; i++) {
            view.getTorpedoImages()[i].setOnDragDetected(dragStarted);
        }

        for (int i = 0; i < 3; i++) {
            view.getSlagschipImages()[i].setOnDragDetected(dragStarted);
        }

        for (int i = 0; i < 4; i++) {
            view.getPatrouilleschipImages()[i].setOnDragDetected(dragStarted);
        }

        // Voeg OnDragOver EventHandler toe aan het target (te bepalen labels op het speelveld)
        GridPane target = view.getGridPaneSpeler();

        target.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                if (dragEvent.getGestureSource() != target && dragEvent.getDragboard().hasImage()) {
                    dragEvent.acceptTransferModes(TransferMode.COPY);
                }
                dragEvent.consume();
            }
        });

        // EventHandler for drop van de SchipImageView view op het speelveld.
        // We laten de oorspronkelijke SchipImageView staan, maar invisibile op die manier kunnen
        // we die later bij het verwijderen van een schip van het bord het schip buiten het bord weer tonen (= isVisible op true zetten).
        // We gebruiken daarvoor ook de TransferMode COPY in plaats van MOVE.
        EventHandler<DragEvent> dragDrop = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                Dragboard dragboard = dragEvent.getDragboard();
                if (dragboard.hasImage()) {
                    SchipImageView source = (SchipImageView) dragEvent.getGestureSource();
                    SpeelvakLabel targetLabel = (SpeelvakLabel) dragEvent.getSource();

                    // Probeer het schip te plaatsen op het speelveld
                    // (faalt bv. indien het schipt met een reeds geplaatst schip)
                    if (!plaatsSchipVoorSpeler(source.getSchipNaam(), source.getSchipId(), targetLabel.getPositie()))
                        return;

                    refreshLabels();
                    dragEvent.setDropCompleted(true);
                    dragEvent.consume();

                    // SchipImageView (onder het bord) verbergen indien drop OK
                    source.setVisible(false);

                    // Activeer klik event om schip te draaien
                    targetLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            if (mouseEvent.isShiftDown()) {
                                Schip schip = model.getBord().getSpeelveldSpeler().getSchip(targetLabel.getPositie());
                                model.getBord().getSpeelveldSpeler().verwijderSchip(schip);
                                view.toonSchip(schip.getNaam(), schip.getId());
                            } else {
                                try {
                                    model.getBord().getSpeelveldSpeler().draaiSchip(targetLabel.getPositie());
                                } catch (ZeeslagException e) {
                                    // TODO melding geven indien schip niet gedraaid kan worden (past niet op bord)
                                    return;
                                }
                            }
                            refreshLabels();
                        }
                    });
                }
            }
        };

        SpeelvakLabel[][] labelsSpeler = view.getLabelsSpeler();
        for (SpeelvakLabel[] speelvakLabels : labelsSpeler) {
            for (SpeelvakLabel speelvakLabel : speelvakLabels) {
                speelvakLabel.setOnDragDropped(dragDrop);
            }
        }

        // We verwijderen de image van de imageview
        EventHandler<DragEvent> dragDone = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                ImageView source = (ImageView) dragEvent.getSource();
                if (dragEvent.getTransferMode() == TransferMode.MOVE) {
                    source.setImage(null);
                }
                dragEvent.consume();
            }
        };

        // Voeg OnDragDone EventHandlers toe aan sources
        view.getVliegdekschipImage().setOnDragDone(dragDone);
        for (int i = 0; i < 2; i++) {
            view.getTorpedoImages()[i].setOnDragDone(dragDone);
        }

        for (int i = 0; i < 3; i++) {
            view.getSlagschipImages()[i].setOnDragDone(dragDone);
        }

        for (int i = 0; i < 4; i++) {
            view.getPatrouilleschipImages()[i].setOnDragDone(dragDone);
        }

        SpeelvakLabel[][] labelsCpu = view.getLabelsCpu();
        for (SpeelvakLabel[] speelvakLabels : labelsCpu) {
            for (SpeelvakLabel aangevallenLabel : speelvakLabels) {
                aangevallenLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (!view.heeftSpelerAlleSchepenGeplaatst()) {
                            view.getInputCommentaar().setText("Gelieve eerst al uw boten te plaatsen!");
                            return;
                        }

                        view.getInputCommentaar().setVisible(false);
                        // Momenteel doen we niets met het resultaat, winnaar van het spel wordt bepaald
                        // door de getWinnaar() method
                        AanvalResultaat aanvalResultaat = model.valAan(aangevallenLabel.getPositie());
                        refreshLabels();

                        Speler winnaar = model.getWinnaar();
                        if (winnaar != null) {
                            Alert alert = new Alert(Alert.AlertType.NONE);
                            {
                                alert.setTitle("Gewonnen!");
                                alert.setHeaderText("Proficiat " + model.getWinnaar().getNaam() + ", u bent de winnaar in " + model.getAantalBeurten() + " beurten!");
                                alert.getButtonTypes().clear();
                                ButtonType ok = new ButtonType("Ok");
                                alert.getButtonTypes().add(ok);
                                alert.showAndWait();
                                if (alert.getResult() == null || alert.getResult().equals(ok)) {
                                    OptiesView optiesView = new OptiesView();
                                    OptiesPresenter optiesPresenter = new OptiesPresenter(model, optiesView);
                                    view.getScene().setRoot(optiesView);
                                    optiesView.getScene();
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    private boolean plaatsSchipVoorSpeler(String schipNaam, int id, String positie) {
        try {
            model.plaatsSchip(model.getSpeler().getNaam(), schipNaam, id, positie, true);
            return true;
        } catch (ZeeslagException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Fout");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return false;
        }
    }

    private void initialiseerSpel() {
        try {
            model.genereerSchepenCpu();
        } catch (ZeeslagException ze) {
            ze.getMessage();
        }

        view.setGrootteSpeelveld(model.getBord().getSpeelveldSpeler().getGrootte());
    }

    private void refreshLabels() {
        view.updateLabelsStatus(model.getBord().getSpeelveldSpeler());
        view.updateLabelsStatus(model.getBord().getSpeelveldCpu());
    }
}
