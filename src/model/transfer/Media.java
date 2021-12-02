package model.transfer;

import java.sql.Blob;

public class Media extends DTO<Integer> {
    private final Blob media;

    public Media(int id, Blob media) {
        this.id = id;
        this.media = media;
    }

    public Blob getMedia() {
        return media;
    }
}
