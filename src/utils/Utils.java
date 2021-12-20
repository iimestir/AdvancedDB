package utils;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import model.MediaType;

import java.io.File;

public class Utils {
    public static String promptPathDialog() {
        FileChooser chooser = new FileChooser();
        File selected = chooser.showOpenDialog(null);

        return selected != null ? selected.getAbsolutePath() : null;
    }

    public static void promptDialog(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setContentText(content);
        alert.setTitle(title);
        alert.setHeaderText(header);

        alert.showAndWait();
    }

    public static MediaType stringToMedia(String str) {
        switch(str) {
            case "IMAGE" -> {
                return MediaType.Picture;
            }
            case "SOUND" -> {
                return MediaType.Sound;
            }
            case "VIDEO" -> {
                return MediaType.Video;
            }
            default -> throw new RuntimeException("Unexpected value : " + str);
        }
    }
}
