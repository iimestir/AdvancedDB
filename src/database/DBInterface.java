package database;

public interface DBInterface {
    void connect();

    void getAllAnimals();

    void getAllAnimalsType();

    void getAllAnimalsByType();
}
