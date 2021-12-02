package database;

import model.BenchmarkedObject;
import model.transfer.*;
import java.util.List;

public class DBPostgres extends DBInterface {
    public DBPostgres() {
        super("ADB_APPLICATION", "jdbc:postgresql://localhost/", "adb_user", "password");
    }

    @Override
    public BenchmarkedObject<List<Animal>> getAllAnimals() {
        return null;
    }

    @Override
    public BenchmarkedObject<List<AnimalType>> getAllTypes() {
        return null;
    }

    @Override
    public BenchmarkedObject<List<Animal>> getAllAnimalsByType(AnimalType type) {
        return null;
    }

    @Override
    public BenchmarkedObject<List<Picture>> getAnimalPicture(Animal animal) {
        return null;
    }

    @Override
    public BenchmarkedObject<List<Video>> getAnimalVideo(Animal animal) {
        return null;
    }

    @Override
    public BenchmarkedObject<List<Sound>> getAnimalSound(Animal animal) {
        return null;
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
