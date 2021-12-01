package database;

import model.BenchmarkedObject;
import model.dto.*;

import java.util.List;

public class DBOracle implements DBInterface {

    @Override
    public void connect() {

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
