package database;

import javafx.util.Pair;
import model.BenchmarkedObject;
import model.MediaType;
import model.transfer.*;

import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DBInterface {
    private final String DB_NAME;
    private final String DB_URL;
    private final String DB_USER;
    private final String DB_PWD;

    protected Connection connection;

    protected DBInterface(String DB_NAME, String DB_URL, String DB_USER, String DB_PWD) {
        this.DB_NAME = DB_NAME;
        this.DB_URL = DB_URL;
        this.DB_USER = DB_USER;
        this.DB_PWD = DB_PWD;
    }

    public Connection connect() throws SQLException {
        if(connection == null)
            connection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PWD);

        return connection;
    }

    public void initialize() throws SQLException {
        connect().setAutoCommit(false);
    }

    public void commit() throws SQLException {
        if(connect().getAutoCommit())
            return;

        connect().commit();
        connect().setAutoCommit(true);
    }

    public void rollback() throws SQLException {
        if(connect().getAutoCommit())
            return;

        connect().rollback();
        connect().setAutoCommit(true);
    }

    protected BenchmarkedObject<List<Animal>> retrieveAnimals(BenchmarkedObject<List<Animal>> result,
                                                            PreparedStatement stmt) throws SQLException {
        List<Animal> selection = new ArrayList<>();

        result.start();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("ID");
            int category = rs.getInt("CATEGORY");
            String name = rs.getString("NAME");
            Date birthDate = rs.getDate("BIRTH");
            String birthPlace = rs.getString("BIRTH_PLACE");
            Date vaccinationDate = rs.getDate("VACCINATION_DATE");
            Date lastVisit = rs.getDate("LAST_VISIT");

            selection.add(new Animal(id, category, name, birthDate, birthPlace, vaccinationDate, lastVisit));
        }

        result.stopAndStoreObject(selection);
        return result;
    }

    protected Pair<String,String> getMediaDBName(MediaType type) {
        switch(type) {
            case Picture -> {
                return new Pair<>("pictures","PHOTO");
            }
            case Video -> {
                return new Pair<>("videos","VIDEO");
            }
            case Sound -> {
                return new Pair<>("sounds","SOUND");
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    // Select - DB data retrievers
    public abstract BenchmarkedObject<List<Animal>> getAllAnimals() throws SQLException;

    public abstract BenchmarkedObject<List<AnimalType>> getAllTypes() throws SQLException;

    public abstract BenchmarkedObject<List<Animal>>  getAllAnimalsByType(AnimalType type) throws SQLException;

    public abstract BenchmarkedObject<List<Picture>>  getAnimalPicture(Animal animal) throws SQLException;

    public abstract BenchmarkedObject<List<Video>>  getAnimalVideo(Animal animal) throws SQLException;

    public abstract BenchmarkedObject<List<Sound>> getAnimalSound(Animal animal) throws SQLException;

    // Insert - DB data inserters
    public abstract BenchmarkedObject<Void> insertType(List<String> types) throws SQLException;

    public abstract BenchmarkedObject<Void> insertAnimal(List<Animal> animals) throws SQLException;

    public abstract BenchmarkedObject<Void> insertMedia(MediaType mediaType, List<Pair<Animal, FileInputStream>> pairs) throws SQLException;
}
