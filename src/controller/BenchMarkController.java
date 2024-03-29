package controller;

import database.DBInterface;
import database.DBOracle;
import database.DBPostgres;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Pair;
import model.BenchmarkedObject;
import model.Database;
import model.MediaType;
import model.transfer.Animal;
import utils.Utils;
import view.Navigator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BenchMarkController implements Initializable {
    @FXML private ComboBox<String> mediaComboBox;
    @FXML private ComboBox<String> operationComboBox;

    @FXML private ListView<String> oracleListView;
    @FXML private ListView<String> pglistView;

    @FXML private ProgressBar progressBar;
    @FXML private Label progressLabel;

    @FXML private TextField pathTextField;
    @FXML private TextField amountTextField;

    @FXML private Button benchButton;
    @FXML private Button pathButton;
    @FXML private Button clearButton;

    private DBInterface database;

    private final Animal testAnimal = new Animal(1,null,null,null,null,null,null);

    private long benchSelect(Database db) throws SQLException, FileNotFoundException {
        if(!connectDB(db))
            return 0;

        int amount = Integer.parseInt(amountTextField.getText());
        MediaType media = Utils.stringToMedia(mediaComboBox.getSelectionModel().getSelectedItem());
        String path = pathTextField.getText();

        List<Pair<Animal, FileInputStream>> list = new ArrayList<>();
        for(int j = 0; j < amount; j++)
            list.add(new Pair<>(testAnimal, new FileInputStream(path)));

        database.insertMedia(media, list);

        BenchmarkedObject bench;
        switch(media) {
            case Picture -> bench = database.getAnimalPicture(testAnimal);
            case Video -> bench = database.getAnimalVideo(testAnimal);
            case Sound -> bench = database.getAnimalSound(testAnimal);
            default -> throw new RuntimeException("Unexpected value " + media);
        }

        database.rollback();

        return bench.getOperationDuration();
    }

    private long benchInsert(Database db) throws SQLException, FileNotFoundException {
        if(!connectDB(db))
            return 0;

        int amount = Integer.parseInt(amountTextField.getText());
        MediaType media = Utils.stringToMedia(mediaComboBox.getSelectionModel().getSelectedItem());
        String path = pathTextField.getText();

        List<Pair<Animal, FileInputStream>> list = new ArrayList<>();
        for(int j = 0; j < amount; j++)
            list.add(new Pair<>(testAnimal, new FileInputStream(path)));

        var bench = database.insertMedia(media, list);

        database.rollback();

        return bench.getOperationDuration();
    }

    private long benchDelete(Database db) throws FileNotFoundException, SQLException {
        if(!connectDB(db))
            return 0;

        int amount = Integer.parseInt(amountTextField.getText());
        MediaType media = Utils.stringToMedia(mediaComboBox.getSelectionModel().getSelectedItem());
        String path = pathTextField.getText();

        List<Pair<Animal, FileInputStream>> list = new ArrayList<>();
        for(int j = 0; j < amount; j++)
            list.add(new Pair<>(testAnimal, new FileInputStream(path)));

        database.insertMedia(media, list);

        // DELETE
        var bench = database.deleteMedia(media,testAnimal);

        database.rollback();

        return bench.getOperationDuration();
    }

    private long benchUpdate(Database db) throws FileNotFoundException, SQLException {
        if(!connectDB(db))
            return 0;

        int amount = Integer.parseInt(amountTextField.getText());
        MediaType media = Utils.stringToMedia(mediaComboBox.getSelectionModel().getSelectedItem());
        String path = pathTextField.getText();

        List<Pair<Animal, FileInputStream>> list = new ArrayList<>();
        for(int j = 0; j < amount; j++)
            list.add(new Pair<>(testAnimal, new FileInputStream(path)));

        database.insertMedia(media, list);

        // DELETE
        var bench = database.updateMedia(media, new FileInputStream(path), testAnimal);

        database.rollback();

        return bench.getOperationDuration();
    }

    private void benchMark() throws SQLException, FileNotFoundException {
        Platform.runLater(this::enableProgress);

        long median_oracle = 0;
        long median_pg = 0;

        switch(operationComboBox.getSelectionModel().getSelectedItem()) {
            case "SELECT" -> {
                for(int i = 0; i < 6; i++) {
                    long oracle = benchSelect(Database.ORACLE);
                    long pg = benchSelect(Database.POSTGRES);

                    median_oracle += oracle;
                    median_pg += pg;

                    int finalI = i+1;

                    Platform.runLater(() -> {
                        oracleListView.getItems().add("SELECT (n=" + finalI + ") = " + oracle + "ms");
                        pglistView.getItems().add("SELECT (n=" + finalI + ") = " + pg + "ms");
                    });

                    Platform.runLater(() -> setProgress(finalI /6.));
                }

                long finalMedian_oracle = median_oracle;
                long finalMedian_pg = median_pg;
                Platform.runLater(() -> {
                    oracleListView.getItems().add("SELECT median = " + (new DecimalFormat("##.##").format(finalMedian_oracle /6.)) + "ms");
                    pglistView.getItems().add("SELECT median = " + (new DecimalFormat("##.##").format(finalMedian_pg /6.)) + "ms");
                });
            }
            case "INSERT" -> {
                for(int i = 0; i < 6; i++) {
                    long oracle = benchInsert(Database.ORACLE);
                    long pg = benchInsert(Database.POSTGRES);

                    median_oracle += oracle;
                    median_pg += pg;

                    int finalI = i+1;

                    Platform.runLater(() -> {
                        oracleListView.getItems().add("INSERT (n=" + finalI + ") = " + oracle + "ms");
                        pglistView.getItems().add("INSERT (n=" + finalI + ") = " + pg + "ms");
                    });

                    Platform.runLater(() -> setProgress(finalI /6.));
                }

                long finalMedian_oracle = median_oracle;
                long finalMedian_pg = median_pg;
                Platform.runLater(() -> {
                    oracleListView.getItems().add("SELECT median = " + (new DecimalFormat("##.##").format(finalMedian_oracle /6.)) + "ms");
                    pglistView.getItems().add("SELECT median = " + (new DecimalFormat("##.##").format(finalMedian_pg /6.)) + "ms");
                });
            }
            case "DELETE" -> {
                for(int i = 0; i < 6; i++) {
                    long oracle = benchDelete(Database.ORACLE);
                    long pg = benchDelete(Database.POSTGRES);

                    median_oracle += oracle;
                    median_pg += pg;

                    int finalI = i+1;

                    Platform.runLater(() -> {
                        oracleListView.getItems().add("DELETE (n=" + finalI + ") = " + oracle + "ms");
                        pglistView.getItems().add("DELETE (n=" + finalI + ") = " + pg + "ms");
                    });

                    Platform.runLater(() -> setProgress(finalI /6.));
                }

                long finalMedian_oracle = median_oracle;
                long finalMedian_pg = median_pg;
                Platform.runLater(() -> {
                    oracleListView.getItems().add("SELECT median = " + (new DecimalFormat("##.##").format(finalMedian_oracle /6.)) + "ms");
                    pglistView.getItems().add("SELECT median = " + (new DecimalFormat("##.##").format(finalMedian_pg /6.)) + "ms");
                });
            }
            case "UPDATE" -> {
                for(int i = 0; i < 6; i++) {
                    long oracle = benchUpdate(Database.ORACLE);
                    long pg = benchUpdate(Database.POSTGRES);

                    median_oracle += oracle;
                    median_pg += pg;

                    int finalI = i+1;

                    Platform.runLater(() -> {
                        oracleListView.getItems().add("UPDATE (n=" + finalI + ") = " + oracle + "ms");
                        pglistView.getItems().add("UPDATE (n=" + finalI + ") = " + pg + "ms");
                    });

                    Platform.runLater(() -> setProgress(finalI /6.));
                }

                long finalMedian_oracle = median_oracle;
                long finalMedian_pg = median_pg;
                Platform.runLater(() -> {
                    oracleListView.getItems().add("SELECT median = " + (new DecimalFormat("##.##").format(finalMedian_oracle /6.)) + "ms");
                    pglistView.getItems().add("SELECT median = " + (new DecimalFormat("##.##").format(finalMedian_pg /6.)) + "ms");
                });
            }
            default -> throw new RuntimeException("Unexpected value");
        }

        Platform.runLater(() -> setProgress(0.));
        Platform.runLater(this::disableProgress);

        database.rollback();
    }

    private void enableProgress() {
        progressBar.setVisible(true);
        progressLabel.setVisible(true);
    }

    private void disableProgress() {
        progressBar.setVisible(false);
        progressLabel.setVisible(false);
    }

    private void setProgress(double progress) {
        progressBar.setProgress(progress);
        progressLabel.setText(new DecimalFormat("##.##").format(progress*100) + "%");
    }

    private void initTextFields() {
        amountTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                amountTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void initButtons() {
        pathButton.setOnAction(e -> pathTextField.setText(Utils.promptPathDialog()));
        benchButton.setOnAction(e -> {
            new Thread(() -> {
                try {
                    Platform.runLater(this::disableControls);
                    benchMark();
                    Platform.runLater(this::enableControls);
                } catch (SQLException | FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }).start();
        });
        clearButton.setOnAction(e -> {
            oracleListView.getItems().clear();
            pglistView.getItems().clear();
        });
    }

    private void enableControls() {
        mediaComboBox.setDisable(false);
        operationComboBox.setDisable(false);
        benchButton.setDisable(false);
        pathButton.setDisable(false);
        amountTextField.setDisable(false);
        clearButton.setDisable(false);
    }

    private void disableControls() {
        mediaComboBox.setDisable(true);
        operationComboBox.setDisable(true);
        benchButton.setDisable(true);
        pathButton.setDisable(true);
        amountTextField.setDisable(true);
        clearButton.setDisable(true);
    }

    private void initComboBox() {
        mediaComboBox.getItems().add("IMAGE");
        mediaComboBox.getItems().add("SOUND");
        mediaComboBox.getItems().add("VIDEO");
        operationComboBox.getItems().add("SELECT");
        operationComboBox.getItems().add("INSERT");
        operationComboBox.getItems().add("UPDATE");
        operationComboBox.getItems().add("DELETE");

        operationComboBox.selectionModelProperty().addListener((observable, oldValue, newValue) -> {
            pathButton.setDisable(newValue.getSelectedItem().equals("SELECT") || newValue.getSelectedItem().equals("DELETE"));
        });
    }

    private boolean connectDB(Database db) {
        database = db == Database.ORACLE ? new DBOracle() : new DBPostgres();

        try {
            database.initialize();
        } catch(SQLException ex) {
            Utils.promptDialog(Alert.AlertType.ERROR, "Error", "Connection failed",
                    "Connection to " + db.name() + " failed");

            database = null;
            return false;
        }

        return true;
    }

    private boolean checkDB() {
        try {
            // ORACLE
            database = new DBOracle();
            database.initialize();

            // PG
            database = new DBPostgres();
            database.initialize();
        } catch(SQLException ex) {
            return false;
        }

        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(!checkDB()) {
            Utils.promptDialog(Alert.AlertType.ERROR, "Error", "Connection failed",
                    "Connection to one of the databases failed");

            Navigator.getInstance().pop();
            return;
        }

        disableProgress();
        initButtons();
        initTextFields();
        initComboBox();
    }
}
