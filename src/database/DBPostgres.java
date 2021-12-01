package database;

import model.BenchmarkedObject;
import model.transfer.*;
import java.util.List;

public class DBPostgres extends DBInterface {
    public DBPostgres() {
        super("ADB_APPLICATION", "jdbc:postgresql://localhost/", "adb_user", "password");
    }

    @Override
    public List<BenchmarkedObject<List<Animal>>> getAllAnimals() {
        return null;
    }

    @Override
    public List<BenchmarkedObject<List<AnimalType>>> getAllTypes() {
        return null;
    }

    @Override
    public List<BenchmarkedObject<List<Animal>>> getAllAnimalsByType(AnimalType type) {
        return null;
    }

    @Override
    public List<BenchmarkedObject<List<Picture>>> getAnimalPicture(Animal animal) {
        return null;
    }

    @Override
    public List<BenchmarkedObject<List<Video>>> getAnimalVideo(Animal animal) {
        return null;
    }

    @Override
    public List<BenchmarkedObject<List<Sound>>> getAnimalSound(Animal animal) {
        return null;
    }

    @Override
    public BenchmarkedObject insertType(String type) {
        return null;
    }

    @Override
    public BenchmarkedObject insertAnimal(String type, Animal animal) {
        return null;
    }

    @Override
    public BenchmarkedObject insertMedia(Animal animal, Media media) {
        return null;
    }
}
