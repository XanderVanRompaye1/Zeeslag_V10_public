package be.kdg.view.optiesView;

import be.kdg.model.Zeeslag;
import be.kdg.view.zeeslagView.ZeeslagPresenter;
import be.kdg.view.zeeslagView.ZeeslagView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.WindowEvent;

/*
 * Xander Van Rompaye
 * 2020-2021
 */
public class OptiesPresenter {
    private OptiesView view;
    private Zeeslag model;

    public OptiesPresenter(Zeeslag model, OptiesView view) {
        this.view = view;
        this.model = model;
        this.addEventHandlers();
    }

    private void addEventHandlers() {
        view.getBtnStartSpel().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int grootte = 10;

                if (view.getCboxGrootteSpeelveld().getSelectionModel().isSelected(0))
                    grootte = 8;
                if (view.getCboxGrootteSpeelveld().getSelectionModel().isSelected(1))
                    grootte = 10;
                if (view.getCboxGrootteSpeelveld().getSelectionModel().isSelected(2))
                    grootte = 12;

                model.startSpel(view.getTfNaam().getText(), grootte);

                if (view.getTfNaam().getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Geen naam ingevuld");
                    alert.setHeaderText("Vul je naam in om door te gaan!");
                    alert.showAndWait();
                    return;
                }

                ZeeslagView zeeslagView = new ZeeslagView(model.getBord().getSpeelveldSpeler().getGrootte());
                ZeeslagPresenter zeeslagPresenter = new ZeeslagPresenter(model, zeeslagView);
                view.getScene().setRoot(zeeslagView);
                zeeslagView.getScene();
            }
        });
    }

    public void voegWindowEventToe() {
        windowCloseEvent(view.getScene());
    }

    private static void windowCloseEvent(Scene scene) {
        scene.getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Weet je zeker dat je het spel wilt stoppen?");
                alert.setTitle("Opgelet");
                alert.getButtonTypes().clear();
                ButtonType ja = new ButtonType("Ja");
                ButtonType nee = new ButtonType("Nee");
                alert.getButtonTypes().addAll(ja, nee);
                alert.showAndWait();
                if (alert.getResult() == null || alert.getResult().equals(nee))
                    windowEvent.consume();
            }
        });
    }
}
