package model.transfer;

public class AnimalType extends DTO<Integer> {
    private final String category;

    public AnimalType(int id, String name) {
        this.id = id;
        this.category = name;
    }

    public String getCategoryName() {
        return category;
    }
}
