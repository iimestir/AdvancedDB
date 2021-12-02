package model.transfer;

import java.sql.Blob;

public class Video extends Media {
    public Video(int id, Blob video) {
        super(id, video);
    }
}
