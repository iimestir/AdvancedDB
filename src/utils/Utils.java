package utils;

import javafx.scene.control.Alert;

public class Utils {
    public static void promptDialog(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setContentText(content);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
}
