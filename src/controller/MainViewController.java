package controller;

import database.DBInterface;
import database.DBOracle;
import database.DBPostgres;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.transfer.Animal;
import model.transfer.AnimalType;
import utils.Utils;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    @FXML private RadioMenuItem oracleMenuItem;
    @FXML private RadioMenuItem postgreMenuItem;
    @FXML private ComboBox<AnimalType> animalCategoryComboBox;
    @FXML private ComboBox<Animal> animalComboBox;

    private DBInterface database;

    private void initMenuItems() {
        oracleMenuItem.setOnAction(e -> {
            database = new DBOracle();

            try {
                database.initialize();
            } catch(SQLException ex) {
                Utils.promptDialog(Alert.AlertType.ERROR, "Erreur", "Connexion échouée",
                        "Connexion à Oracle impossible");

                database = null;
                oracleMenuItem.setSelected(false);
                return;
            }

            Utils.promptDialog(Alert.AlertType.CONFIRMATION, "Oracle", "Base de donnée sélectionnée",
                    "Connexion à Oracle établie");

        });

        postgreMenuItem.setOnAction(e -> {
            database = new DBPostgres();

            try {
                database.initialize();
            } catch(SQLException ex) {
                Utils.promptDialog(Alert.AlertType.ERROR, "Erreur", "Connexion échouée",
                        "Connexion à Postgres impossible");

                database = null;
                postgreMenuItem.setSelected(false);
                return;
            }

            Utils.promptDialog(Alert.AlertType.CONFIRMATION, "Oracle", "Base de donnée sélectionnée",
                    "Connexion à Postgres établie");
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initMenuItems();
    }
}
