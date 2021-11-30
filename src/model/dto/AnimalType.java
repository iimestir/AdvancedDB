package model.dto;

public class AnimalType extends DTO<Integer> {
    private String category;

    public AnimalType(int id, String name) {
        this.id = id;
        this.category = name;
    }
}
