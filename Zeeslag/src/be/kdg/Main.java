package be.kdg;

import be.kdg.model.Zeeslag;
import be.kdg.view.optiesView.OptiesPresenter;
import be.kdg.view.optiesView.OptiesView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/*
 * Xander Van Rompaye
 * 2020-2021
 */
public class Main extends Application {
    public static void main(String[] args) {
        javafx.application.Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        Zeeslag model = new Zeeslag();
        OptiesView optiesView = new OptiesView();
        OptiesPresenter optiesPresenter = new OptiesPresenter(model, optiesView);
        Scene scene = new Scene(optiesView);
        stage.setScene(scene);
        stage.setTitle("Zeeslag");
        stage.setHeight(850);
        stage.setWidth(1400);
        stage.setResizable(false);
        optiesPresenter.voegWindowEventToe();

        scene.getStylesheets().add("/stylesheets/stijl.css");
        Image application = new Image(getClass().getResourceAsStream("/images/favicon.jpg"));
        stage.getIcons().addAll(application);

        stage.show();
    }
}
