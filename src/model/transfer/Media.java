package model.transfer;

import java.sql.Blob;

public class Media extends DTO<Integer> {
    private final Blob media;

    public Media(Integer id, Blob media) {
        this.id = id;
        this.media = media;
    }

    public Blob getMedia() {
        return media;
    }
}
