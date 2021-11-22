package view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.Disposable;
import model.Panel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Singleton class Navigator
 *
 * Used to navigate between scenes
 */
public class Navigator<T> {
    private static final Navigator instance = new Navigator();

    private final Scene scene = new Scene(new Pane());
    private final List<Pair<Parent, T>> panels = new ArrayList<>();

    /**
     * Used to prevent any instantiating
     */
    private Navigator() {}

    /**
     * Returns the singleton instance
     * @return singleton instance
     */
    public static Navigator getInstance() { return instance; }

    /**
     * Used to register the navigator local scene to the current main stage
     * @param stage the current main stage
     * @param panel first panel
     */
    public void register(Stage stage, Panel panel) {
        stage.setScene(this.scene);

        this.push(panel);
        stage.show();
    }

    /**
     * Used to push a new panel to the navigator
     * @param panel panel
     */
    public void push(Panel panel) {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + panel.getPath()),
                ResourceBundle.getBundle("translation", Utils.CURRENT_LOCALE));

        try {
            final Parent parent = loader.load();

            this.panels.add(new Pair<>(parent,loader.getController()));
            this.scene.setRoot(parent);

            if(panels.size() >= 2 && panels.get(panels.size()-2).getValue() instanceof Disposable)
                ((Disposable) panels.get(panels.size()-2).getValue()).pause();

        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Used to remove the last panel from the navigator
     */
    public void pop() {
        if(panels.get(panels.size()-1).getValue() instanceof Disposable)
            ((Disposable) panels.get(panels.size()-1).getValue()).pause();

        this.panels.remove(panels.size()-1);
        this.scene.setRoot(panels.get(panels.size()-1).getKey());
    }

    /**
     * Dispose all disposables
     */
    public void exit() {
        panels.forEach(p -> {
            if(p.getValue() instanceof Disposable)
                ((Disposable) p.getValue()).dispose();
        });

        Platform.exit();
    }

}