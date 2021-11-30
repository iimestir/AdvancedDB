package database;

import model.dto.*;

import java.util.List;

public class DBOracle implements DBInterface {
    @Override
    public void connect() {

    }

    @Override
    public List<Animal> getAllAnimals() {
        return null;
    }

    @Override
    public List<AnimalType> getAllTypes() {
        return null;
    }

    @Override
    public List<Animal> getAllAnimalsByType(AnimalType type) {
        return null;
    }

    @Override
    public List<Picture> getAnimalPicture(Animal animal) {
        return null;
    }

    @Override
    public List<Video> getAnimalVideo(Animal animal) {
        return null;
    }

    @Override
    public List<Sound> getAnimalSound(Animal animal) {
        return null;
    }

    @Override
    public double insertType(String type) {
        return 0;
    }

    @Override
    public double insertAnimal(String type, Animal animal) {
        return 0;
    }

    @Override
    public double insertMedia(Animal animal, Media media) {
        return 0;
    }
}
