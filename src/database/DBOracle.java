package database;

import javafx.util.Pair;
import model.BenchmarkedObject;
import model.MediaType;
import model.transfer.*;

import java.io.FileInputStream;
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

    @Override
    public BenchmarkedObject<List<Picture>> getAnimalPicture(Animal animal) throws SQLException {
        BenchmarkedObject<List<Picture>> result = new BenchmarkedObject<>();
        List<Picture> selection = new ArrayList<>();

        String request = "SELECT * FROM pictures WHERE ID = ? ";
        PreparedStatement stmt = connection.prepareStatement(request);
        stmt.setInt(1, animal.getId());

        result.start();

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("ID");
            Blob picture = rs.getBlob("PHOTO");

            selection.add(new Picture(id, picture));
        }

        result.stopAndStoreObject(selection);
        return result;
    }

    @Override
    public BenchmarkedObject<List<Video>> getAnimalVideo(Animal animal) throws SQLException {
        BenchmarkedObject<List<Video>> result = new BenchmarkedObject<>();
        List<Video> selection = new ArrayList<>();

        String request = "SELECT * FROM videos WHERE ID = ? ";
        PreparedStatement stmt = connection.prepareStatement(request);
        stmt.setInt(1, animal.getId());

        result.start();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("ID");
            Blob video = rs.getBlob("VIDEO");

            selection.add(new Video(id, video));
        }

        result.stopAndStoreObject(selection);
        return result;
    }

    @Override
    public BenchmarkedObject<List<Sound>> getAnimalSound(Animal animal) throws SQLException {
        BenchmarkedObject<List<Sound>> result = new BenchmarkedObject<>();
        List<Sound> selection = new ArrayList<>();

        String request = "SELECT * FROM sounds WHERE ID = ? ";
        PreparedStatement stmt = connection.prepareStatement(request);
        stmt.setInt(1, animal.getId());

        result.start();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("ID");
            Blob sound = rs.getBlob("SOUND");

            selection.add(new Sound(id, sound));
        }

        result.stopAndStoreObject(selection);
        return result;
    }

    @Override
    public BenchmarkedObject<Void> insertType(List<String> types) throws SQLException {
        BenchmarkedObject<Void> result = new BenchmarkedObject<>();
        String request = "INSERT INTO animal_type(NAME) VALUES(?)";

        result.start();

        for(String type : types) {
            PreparedStatement stmt = connection.prepareStatement(request);
            stmt.setString(1, type);

            stmt.executeUpdate();
        }

        result.stopAndStoreObject(null);
        return result;
    }

    @Override
    public BenchmarkedObject<Void> insertAnimal(List<Animal> animals) throws SQLException {
        BenchmarkedObject<Void> result = new BenchmarkedObject<>();
        String request = "INSERT INTO animals(CATEGORY,NAME,BIRTH,BIRTH_PLACE,VACCINATION_DATE,LAST_VISIT) " +
                "VALUES(?,?,?,?,?,?)";

        result.start();

        for(Animal animal : animals) {
            PreparedStatement stmt = connection.prepareStatement(request);
            stmt.setInt(1, animal.getCategory());
            stmt.setString(2, animal.getName());
            stmt.setDate(3, animal.getBirthDate());
            stmt.setString(4, animal.getBirthPlace());
            stmt.setDate(5, animal.getVaccinationDate());
            stmt.setDate(6, animal.getLastVisit());

            stmt.executeUpdate();
        }

        result.stopAndStoreObject(null);
        return result;
    }

    @Override
    public BenchmarkedObject<Void> insertMedia(MediaType mediaType, List<Pair<Animal, FileInputStream>> pairs) throws SQLException {
        BenchmarkedObject<Void> result = new BenchmarkedObject<>();
        result.start();

        Pair<String, String> mediaDB = getMediaDBName(mediaType);

        String request = "INSERT INTO " + mediaDB.getKey() + " (ID," + mediaDB.getValue() + ") " +
                "VALUES(?,?)";
        PreparedStatement stmt = connection.prepareStatement(request);

        int i = 0;
        for(Pair<Animal, FileInputStream> pair : pairs) {
            stmt.setInt(1, pair.getKey().getId());
            stmt.setBlob(2, pair.getValue());

            stmt.addBatch();

            if(i++%200 == 0) {
                stmt.executeBatch();
            }

            stmt.clearParameters();
        }
        stmt.close();

        result.stopAndStoreObject(null);
        return result;
    }
}
