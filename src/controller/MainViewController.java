package controller;

import database.DBInterface;
import database.DBOracle;
import database.DBPostgres;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import model.Database;
import model.transfer.*;
import utils.Utils;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainViewController implements Initializable {
    @FXML private ToggleGroup dbgroup;
    @FXML private RadioMenuItem oracleMenuItem;
    @FXML private RadioMenuItem postgreMenuItem;
    @FXML private ComboBox<AnimalType> animalCategoryComboBox;
    @FXML private ComboBox<Animal> animalComboBox;

    @FXML private Tab picturesTab;
    @FXML private Tab videosTab;
    @FXML private Tab soundsTab;
    @FXML private AnchorPane picturesPane;
    @FXML private AnchorPane videosPane;
    @FXML private AnchorPane soundsPane;
    @FXML private TextField idField;
    @FXML private TextField categoryField;
    @FXML private TextField nameField;
    @FXML private TextField birthplaceField;
    @FXML private TextField birthdateField;
    @FXML private TextField vaccinationField;
    @FXML private TextField lastVisitField;

    private DBInterface database;

    private void fillAnimalTypeComboBox() {
        animalCategoryComboBox.getItems().setAll(database.getAllTypes().getObject());
    }

    private void fillAnimalComboBox(AnimalType type) {
        animalComboBox.getItems().setAll(database.getAllAnimalsByType(type).getObject());
    }

    private void fillAnimalComboBox() {
        animalComboBox.getItems().setAll(database.getAllAnimals().getObject());
    }

    private void fillAnimalPictures(Animal animal) {
        List<Picture> pictures = database.getAnimalPicture(animal).getObject();

        // TODO : Pictures controller à tester
        picturesPane.getChildren().setAll(pictures.stream().map(e -> {
            try {
                InputStream is = e.getMedia().getBinaryStream();
                BufferedImage read = ImageIO.read(is);
                return new ImageView(SwingFXUtils.toFXImage(read, null));
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }

            return null;
        }).collect(Collectors.toList()));
    }

    private void fillAnimalVideos(Animal animal) {
        // TODO : Controller video
    }

    private void fillAnimalSounds(Animal animal) {
        List<Sound> sounds = database.getAnimalSound(animal).getObject();

        // TODO : Sound controller à tester
        soundsPane.getChildren().setAll(sounds.stream().map(e -> {
            Button btn = new Button("Sound");
            btn.setOnAction(event -> {
                try {
                    InputStream is = e.getMedia().getBinaryStream();
                    AudioInputStream as = AudioSystem.getAudioInputStream(is);
                    AudioFormat format = as.getFormat();
                    DataLine.Info info = new DataLine.Info(Clip.class, format);

                    Clip audioClip = (Clip) AudioSystem.getLine(info);
                    audioClip.open(as);
                    audioClip.start();
                } catch (SQLException | UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            });

            return btn;
        }).collect(Collectors.toList()));
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

    private void connectDB(Database db) {
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

    private void initMenuItems() {
        oracleMenuItem.setOnAction(e -> connectDB(Database.ORACLE));
        postgreMenuItem.setOnAction(e -> connectDB(Database.POSTGRES));
    }

    private void initTabs() {
        picturesTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Animal animal = animalComboBox.getSelectionModel().getSelectedItem();

            if(animal != null)
                fillAnimalPictures(animal);
        });

        videosTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Animal animal = animalComboBox.getSelectionModel().getSelectedItem();

            if(animal != null)
                fillAnimalVideos(animal);
        });

        soundsTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Animal animal = animalComboBox.getSelectionModel().getSelectedItem();

            if(animal != null)
                fillAnimalSounds(animal);
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

        animalCategoryComboBox.selectionModelProperty().addListener((observable, oldValue, newValue) ->
                fillAnimalComboBox(newValue.getSelectedItem()));

        animalComboBox.selectionModelProperty().addListener((observable, oldValue, newValue) ->
                fillAnimalTab(newValue.getSelectedItem()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initMenuItems();
        initComboBoxes();
        initTabs();
    }
}
