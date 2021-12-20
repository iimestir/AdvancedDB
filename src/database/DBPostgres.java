package database;

import javafx.util.Pair;
import model.BenchmarkedObject;
import model.MediaType;
import model.transfer.*;

import javax.sql.rowset.serial.SerialBlob;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBPostgres extends DBInterface {
    public DBPostgres() {
        super("ADB_APPLICATION", "jdbc:postgresql://localhost/", "adb_user", "1234");
    }

    @Override
    public String whoAmI() {
        return "POSTGRES";
    }

    @Override
    public BenchmarkedObject<List<Animal>> getAllAnimals() throws SQLException {
        BenchmarkedObject<List<Animal>> result = new BenchmarkedObject<>();

        String request = "SELECT * FROM Public.\"animal\"";
        PreparedStatement stmt = connection.prepareStatement(request);

        return retrieveAnimals(result, stmt);
    }

    @Override
    public BenchmarkedObject<List<AnimalType>> getAllTypes() throws SQLException {
        BenchmarkedObject<List<AnimalType>> result = new BenchmarkedObject<>();
        List<AnimalType> results = new ArrayList<>();

        String request = "SELECT * FROM Public.\"animal_type\"";
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

        String request = "SELECT * FROM Public.\"animals\" WHERE \"CATEGORY\" = ?";
        PreparedStatement stmt = connection.prepareStatement(request);
        stmt.setInt(1, type.getId());

        return retrieveAnimals(result, stmt);
    }

    @Override
    public BenchmarkedObject<List<Picture>> getAnimalPicture(Animal animal) throws SQLException {
        BenchmarkedObject<List<Picture>> result = new BenchmarkedObject<>();
        List<Picture> selection = new ArrayList<>();

        String request = "SELECT * FROM Public.\"pictures\" WHERE \"ID\" = ? ";
        PreparedStatement stmt = connection.prepareStatement(request);
        stmt.setInt(1, animal.getId());

        result.start();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("ID");
            InputStream picture = rs.getBinaryStream("PHOTO");

            Blob blob = null;
            try {
                blob = new SerialBlob(picture.readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            selection.add(new Picture(id, blob));
        }

        result.stopAndStoreObject(selection);
        return result;
    }

    @Override
    public BenchmarkedObject<List<Video>> getAnimalVideo(Animal animal) throws SQLException {
        BenchmarkedObject<List<Video>> result = new BenchmarkedObject<>();
        List<Video> selection = new ArrayList<>();

        String request = "SELECT * FROM Public.\"videos\" WHERE \"ID\" = ? ";
        PreparedStatement stmt = connection.prepareStatement(request);
        stmt.setInt(1, animal.getId());

        result.start();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("ID");
            InputStream video = rs.getBinaryStream("VIDEO");

            Blob blob = null;
            try {
                blob = new SerialBlob(video.readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            selection.add(new Video(id, blob));
        }

        result.stopAndStoreObject(selection);
        return result;
    }

    @Override
    public BenchmarkedObject<List<Sound>> getAnimalSound(Animal animal) throws SQLException {
        BenchmarkedObject<List<Sound>> result = new BenchmarkedObject<>();
        List<Sound> selection = new ArrayList<>();

        String request = "SELECT * FROM Public.\"sounds\" WHERE \"ID\" = ? ";
        PreparedStatement stmt = connection.prepareStatement(request);
        stmt.setInt(1, animal.getId());

        result.start();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("ID");
            InputStream sound = rs.getBinaryStream("SOUND");

            Blob blob = null;
            try {
                blob = new SerialBlob(sound.readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            selection.add(new Sound(id, blob));
        }

        result.stopAndStoreObject(selection);
        return result;
    }

    @Override
    public BenchmarkedObject<Void> insertType(List<String> types) throws SQLException {
        BenchmarkedObject<Void> result = new BenchmarkedObject<>();
        String request = "INSERT INTO Public.\"animal_type\"(\"NAME\") VALUES(?)";

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
        String request = "INSERT INTO Public.\"animals\"(\"CATEGORY\",\"NAME\",\"BIRTH\",\"BIRTH_PLACE\",\"VACCINATION_DATE\",\"LAST_VISIT\") " +
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

        String request = "INSERT INTO \"" + mediaDB.getKey() + "\" (\"ID\",\"" + mediaDB.getValue() + "\") " +
                "VALUES(?,?)";
        PreparedStatement stmt = connection.prepareStatement(request);

        int i = 0;
        for(Pair<Animal, FileInputStream> pair : pairs) {
            stmt.setInt(1, pair.getKey().getId());
            stmt.setBinaryStream(2, pair.getValue());

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

    @Override
    public BenchmarkedObject<Void> deleteMedia(MediaType mediaType, Animal animal) throws SQLException {
        BenchmarkedObject<Void> result = new BenchmarkedObject<>();
        result.start();

        Pair<String, String> mediaDB = getMediaDBName(mediaType);

        String request = "DELETE FROM Public.\"" + mediaDB.getKey() + "\" WHERE \"ID\" = ?";
        PreparedStatement stmt = connection.prepareStatement(request);
        stmt.setInt(1, animal.getId());

        stmt.executeUpdate();

        result.stopAndStoreObject(null);
        return result;
    }

    @Override
    public BenchmarkedObject<Void> updateMedia(MediaType mediaType, FileInputStream media, Animal animal) throws SQLException {
        BenchmarkedObject<Void> result = new BenchmarkedObject<>();
        result.start();

        Pair<String, String> mediaDB = getMediaDBName(mediaType);

        String request = "UPDATE Public.\"" + mediaDB.getKey() + "\" SET \"" + mediaDB.getValue() + "\" = ? WHERE \"ID\" = ?";
        PreparedStatement stmt = connection.prepareStatement(request);
        stmt.setBinaryStream(1, media);
        stmt.setInt(2, animal.getId());

        stmt.executeUpdate();

        result.stopAndStoreObject(null);
        return result;
    }
}
