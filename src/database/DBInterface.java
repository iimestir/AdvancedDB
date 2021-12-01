package database;

import model.BenchmarkedObject;
import model.dto.*;

import java.util.List;

public interface DBInterface {
    void connect();

    // Select - DB data retrievers
    List<BenchmarkedObject<List<Animal>>> getAllAnimals();

    List<BenchmarkedObject<List<AnimalType>>> getAllTypes();

    List<BenchmarkedObject<List<Animal>>>  getAllAnimalsByType(AnimalType type);

    List<BenchmarkedObject<List<Picture>>>  getAnimalPicture(Animal animal);

    List<BenchmarkedObject<List<Video>>>  getAnimalVideo(Animal animal);

    List<BenchmarkedObject<List<Sound>>> getAnimalSound(Animal animal);

    // Insert - DB data inserters
    BenchmarkedObject<Void> insertType(String type);

    BenchmarkedObject<Void> insertAnimal(String type, Animal animal);

    BenchmarkedObject<Void> insertMedia(Animal animal, Media media);
}
