package view.dialog;

import controller.BenchMarkController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.io.IOException;

public class BenchMarkDialog extends Dialog {
    private static final String FXML_PATH = "/fxml/benchmark.fxml";

    private BenchMarkDialog() throws IOException {
        setTitle("BenchMark");
        setResizable(false);

        final FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH));

        getDialogPane().setContent(loader.load());

        final BenchMarkController controller = loader.getController();

        getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
    }

    public static BenchMarkDialog prompt() throws IOException {
        return new BenchMarkDialog();
    }
}
