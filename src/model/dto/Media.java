package model.dto;

public class Media extends DTO<Integer> {
    private final byte[] media;

    public Media(int id, byte[] media) {
        this.id = id;
        this.media = media;
    }

    public byte[] getMedia() {
        return media;
    }
}
