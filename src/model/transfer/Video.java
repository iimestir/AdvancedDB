package model.transfer;

import java.sql.Blob;

public class Video extends Media {
    public Video(int id, Blob video) {
        super(id, video);
    }

    public Video(Blob video) {
        super(null, video);
    }

    @Override
    public String toString() {
        return this.id + "\tVideo";
    }
}
