package database;

import model.dto.*;

import java.util.List;

public interface DBInterface {
    void connect();

    // Select - DB data retrievers
    List<Animal> getAllAnimals();

    List<AnimalType> getAllTypes();

    List<Animal> getAllAnimalsByType(AnimalType type);

    List<Picture> getAnimalPicture(Animal animal);

    List<Video> getAnimalVideo(Animal animal);

    List<Sound> getAnimalSound(Animal animal);

    // Insert - DB data inserters
    double insertType(String type);

    double insertAnimal(String type, Animal animal);

    double insertMedia(Animal animal, Media media);
}
