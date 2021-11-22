package main;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import view.Navigator;
import model.Panel;

public class FXApp extends Application {
    @Override
    public void start(Stage stage) {
        // Window settings
        stage.setWidth(800);
        stage.setHeight(500);
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        //stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/icon/covid19.png")));
        stage.setTitle("Vet View");

        Navigator.getInstance().register(stage, Panel.MAIN);
    }

    public static void start(String[] args) {
        launch(args);
    }
}
