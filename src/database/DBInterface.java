package database;

import model.BenchmarkedObject;
import model.transfer.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public abstract class DBInterface {
    private final String DB_NAME;
    private final String DB_URL;
    private final String DB_USER;
    private final String DB_PWD;

    private Connection connection;

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

    // Select - DB data retrievers
    abstract List<BenchmarkedObject<List<Animal>>> getAllAnimals();

    abstract List<BenchmarkedObject<List<AnimalType>>> getAllTypes();

    abstract List<BenchmarkedObject<List<Animal>>>  getAllAnimalsByType(AnimalType type);

    abstract List<BenchmarkedObject<List<Picture>>>  getAnimalPicture(Animal animal);

    abstract List<BenchmarkedObject<List<Video>>>  getAnimalVideo(Animal animal);

    abstract List<BenchmarkedObject<List<Sound>>> getAnimalSound(Animal animal);

    // Insert - DB data inserters
    abstract BenchmarkedObject<Void> insertType(String type);

    abstract BenchmarkedObject<Void> insertAnimal(String type, Animal animal);

    abstract BenchmarkedObject<Void> insertMedia(Animal animal, Media media);
}
