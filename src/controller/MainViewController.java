package controller;

import database.DBInterface;
import database.DBOracle;
import database.DBPostgres;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaView;
import javafx.util.Callback;
import model.Database;
import model.transfer.*;
import utils.Utils;
import view.dialog.BenchMarkDialog;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    @FXML private ToggleGroup dbgroup;
    @FXML private RadioMenuItem oracleMenuItem;
    @FXML private RadioMenuItem postgreMenuItem;
    @FXML private ComboBox<AnimalType> animalCategoryComboBox;
    @FXML private ComboBox<Animal> animalComboBox;

    @FXML private TabPane tabPane;
    @FXML private Tab overviewTab;
    @FXML private Tab picturesTab;
    @FXML private Tab videosTab;
    @FXML private Tab soundsTab;
    @FXML private TextField idField;
    @FXML private TextField categoryField;
    @FXML private TextField nameField;
    @FXML private TextField birthplaceField;
    @FXML private TextField birthdateField;
    @FXML private TextField vaccinationField;
    @FXML private TextField lastVisitField;

    @FXML private ComboBox<Picture> pictureComboBox;
    @FXML private ImageView imageView;
    @FXML private ComboBox<Video> videoComboBox;
    @FXML private MediaView mediaView;
    @FXML private ComboBox<Sound> soundComboBox;
    @FXML private Button playButton;

    private SimpleObjectProperty<Animal> selectedAnimal;

    private DBInterface database;

    private void fillAnimalTypeComboBox() throws SQLException {
        var ee = database.getAllTypes();
        animalCategoryComboBox.setItems(FXCollections.observableArrayList(ee.getObject()));
    }

    private void fillAnimalComboBox(AnimalType type) throws SQLException {
        var ee = database.getAllAnimalsByType(type);
        animalComboBox.setItems(FXCollections.observableArrayList(ee.getObject()));
    }

    private void fillAnimalPicturesComboBox(Animal animal) throws SQLException {
        var ee = database.getAnimalPicture(animal);
        pictureComboBox.setItems(FXCollections.observableArrayList(ee.getObject()));
    }

    private void fillAnimalVideosComboBox(Animal animal) throws SQLException {
        var ee = database.getAnimalVideo(animal);
        videoComboBox.setItems(FXCollections.observableArrayList(ee.getObject()));
    }

    private void fillAnimalSoundsComboBox(Animal animal) throws SQLException {
        var ee = database.getAnimalSound(animal);
        soundComboBox.setItems(FXCollections.observableArrayList(ee.getObject()));
    }

    private void fillAnimalTab(Animal animal) {
        idField.setText(animal.getId().toString());
        categoryField.setText(animal.getCategory().toString());
        nameField.setText(animal.getName());
        birthplaceField.setText(animal.getBirthPlace());
        birthdateField.setText(animal.getBirthDate().toString());
        vaccinationField.setText(animal.getVaccinationDate().toString());
        lastVisitField.setText(animal.getLastVisit().toString());
    }

    private void connectDB(Database db) throws SQLException {
        database = db == Database.ORACLE ? new DBOracle() : new DBPostgres();

        try {
            database.initialize();
        } catch(SQLException ex) {
            Utils.promptDialog(Alert.AlertType.ERROR, "Error", "Connection failed",
                    "Connection to " + db.name() + " failed");

            database = null;
            oracleMenuItem.setSelected(false);
            postgreMenuItem.setSelected(false);

            return;
        }

        Utils.promptDialog(Alert.AlertType.CONFIRMATION, db.name(), "Database selected",
                "Connection to " + db.name() + " established");
        fillAnimalTypeComboBox();
    }

    public void callBenchMarkDialog(MouseEvent mouseEvent) throws IOException {
        BenchMarkDialog.prompt().showAndWait();
    }

    private void initMenuItems() {
        oracleMenuItem.setOnAction(e -> {
            try {
                connectDB(Database.ORACLE);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        postgreMenuItem.setOnAction(e -> {
            try {
                connectDB(Database.POSTGRES);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void initTabs() {
        picturesTab.setDisable(true);
        videosTab.setDisable(true);
        soundsTab.setDisable(true);

        picturesTab.setOnSelectionChanged(e -> {
            if(selectedAnimal.get() != null) {
                try {
                    fillAnimalPicturesComboBox(selectedAnimal.get());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        videosTab.setOnSelectionChanged(e -> {
            if(selectedAnimal.get() != null) {
                try {
                    fillAnimalVideosComboBox(selectedAnimal.get());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        soundsTab.setOnSelectionChanged(e -> {
            if(selectedAnimal.get() != null) {
                try {
                    fillAnimalSoundsComboBox(selectedAnimal.get());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        selectedAnimal.addListener((observable, oldValue, newValue) -> {
            tabPane.getSelectionModel().select(overviewTab);

            if(selectedAnimal.get() != null) {
                picturesTab.setDisable(false);
                videosTab.setDisable(false);
                soundsTab.setDisable(false);
            } else {
                picturesTab.setDisable(true);
                videosTab.setDisable(true);
                soundsTab.setDisable(true);

                pictureComboBox.getItems().clear();
                soundComboBox.getItems().clear();
                videoComboBox.getItems().clear();

                imageView.setImage(null);
                mediaView.setMediaPlayer(null);
                playButton.setOnAction(e -> { });
            }
        });
    }

    private void initComboBoxes() {
        animalCategoryComboBox.setDisable(true);
        animalComboBox.setDisable(true);

        dbgroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if(!oracleMenuItem.isSelected() && !postgreMenuItem.isSelected()) {
                animalCategoryComboBox.setDisable(true);
                animalComboBox.setDisable(true);
            } else {
                animalCategoryComboBox.setDisable(false);
                animalComboBox.setDisable(false);
            }
        });

        animalCategoryComboBox.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            if(newValue == null)
                return;

            selectedAnimal.set(null);

            try {
                fillAnimalComboBox(newValue);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        animalComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null)
                return;

            selectedAnimal.setValue(newValue);
            fillAnimalTab(newValue);
        });

        initComboBoxesCallback();
    }

    private void initComboBoxesCallback() {
        animalCategoryComboBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<AnimalType> call(ListView<AnimalType> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(AnimalType item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getId() + "\t" + item.getCategoryName());
                        }
                    }
                };
            }
        });

        animalComboBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Animal> call(ListView<Animal> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Animal item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getId() + "\t" + item.getName());
                        }
                    }
                };
            }
        });
    }

    private void initInnerComboBoxes() {
        pictureComboBox.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            if(newValue == null)
                return;

            try {
                InputStream is = newValue.getMedia().getBinaryStream();

                BufferedImage read = ImageIO.read(is);
                imageView.setImage(SwingFXUtils.toFXImage(read, null));
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        });

        soundComboBox.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            if(newValue == null)
                return;

            playButton.setOnAction(e -> {
                try {
                    InputStream is = newValue.getMedia().getBinaryStream();

                    AudioInputStream as = AudioSystem.getAudioInputStream(is);
                    AudioFormat format = as.getFormat();
                    DataLine.Info info = new DataLine.Info(Clip.class, format);

                    Clip audioClip = (Clip) AudioSystem.getLine(info);
                    audioClip.open(as);
                    audioClip.start();
                } catch (IOException | SQLException | UnsupportedAudioFileException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            });
        });

        videoComboBox.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            if(newValue == null)
                return;

            // TODO : Videos
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedAnimal = new SimpleObjectProperty<>();

        initMenuItems();
        initComboBoxes();
        initInnerComboBoxes();
        initTabs();
    }
}
