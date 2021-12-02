package model.transfer;

import java.sql.Blob;

public class Sound extends Media {
    public Sound(int id, Blob sound) {
        super(id, sound);
    }
}
