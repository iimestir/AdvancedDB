package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import model.dto.Animal;
import model.dto.AnimalType;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    @FXML private ComboBox<AnimalType> animalCategoryComboBox;
    @FXML private ComboBox<Animal> animalComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
