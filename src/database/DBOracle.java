package database;

import model.BenchmarkedObject;
import model.transfer.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBOracle extends DBInterface {
    public DBOracle() {
        super("orcl2", "jdbc:oracle:thin:@localhost:1521:", "SYSTEM", "1234");
    }

    @Override
    public BenchmarkedObject<List<Animal>> getAllAnimals() throws SQLException {
        BenchmarkedObject<List<Animal>> result = new BenchmarkedObject<>();

        String request = "SELECT * FROM animals";
        PreparedStatement stmt = connection.prepareStatement(request);

        return retrieveAnimals(result, stmt);
    }

    @Override
    public BenchmarkedObject<List<AnimalType>> getAllTypes() throws SQLException {
        BenchmarkedObject<List<AnimalType>> result = new BenchmarkedObject<>();
        List<AnimalType> results = new ArrayList<>();

        String request = "SELECT * FROM animal_type";
        PreparedStatement stmt = connection.prepareStatement(request);

        result.start();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("ID");
            String category = rs.getString("CATEGORY");

            results.add(new AnimalType(id, category));
        }

        result.stopAndStoreObject(results);
        return result;
    }

    @Override
    public BenchmarkedObject<List<Animal>> getAllAnimalsByType(AnimalType type) throws SQLException {
        BenchmarkedObject<List<Animal>> result = new BenchmarkedObject<>();

        String request = "SELECT * FROM animals WHERE CATEGORY = ?";
        PreparedStatement stmt = connection.prepareStatement(request);
        stmt.setInt(1, type.getId());

        return retrieveAnimals(result, stmt);
    }

    private BenchmarkedObject<List<Animal>> retrieveAnimals(BenchmarkedObject<List<Animal>> result,
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

    @Override
    public BenchmarkedObject<List<Picture>> getAnimalPicture(Animal animal) throws SQLException {
        BenchmarkedObject<List<Picture>> result = new BenchmarkedObject<>();
        List<Picture> results = new ArrayList<>();

        String request = "SELECT * FROM pictures WHERE ID = ? ";
        PreparedStatement stmt = connection.prepareStatement(request);
        stmt.setInt(1, animal.getId());

        result.start();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("ID");
            Blob picture = rs.getBlob("PHOTO");

            results.add(new Picture(id, picture));
        }

        result.stopAndStoreObject(results);
        return result;
    }

    @Override
    public BenchmarkedObject<List<Video>> getAnimalVideo(Animal animal) throws SQLException {
        BenchmarkedObject<List<Video>> result = new BenchmarkedObject<>();
        List<Video> results = new ArrayList<>();

        String request = "SELECT * FROM videos WHERE ID = ? ";
        PreparedStatement stmt = connection.prepareStatement(request);
        stmt.setInt(1, animal.getId());

        result.start();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("ID");
            Blob video = rs.getBlob("VIDEO");

            results.add(new Video(id, video));
        }

        result.stopAndStoreObject(results);
        return result;
    }

    @Override
    public BenchmarkedObject<List<Sound>> getAnimalSound(Animal animal) throws SQLException {
        BenchmarkedObject<List<Sound>> result = new BenchmarkedObject<>();
        List<Sound> results = new ArrayList<>();

        String request = "SELECT * FROM sounds WHERE ID = ? ";
        PreparedStatement stmt = connection.prepareStatement(request);
        stmt.setInt(1, animal.getId());

        result.start();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("ID");
            Blob sound = rs.getBlob("SOUND");

            results.add(new Sound(id, sound));
        }

        result.stopAndStoreObject(results);
        return result;
    }

    @Override
    public BenchmarkedObject<Void> insertType(String type) {
        return null;
    }

    @Override
    public BenchmarkedObject<Void> insertAnimal(String type, Animal animal) {
        return null;
    }

    @Override
    public BenchmarkedObject<Void> insertMedia(Animal animal, Media media) {
        return null;
    }
}
