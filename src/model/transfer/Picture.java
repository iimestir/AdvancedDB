package model.transfer;

import java.sql.Blob;

public class Picture extends Media {
    public Picture(int id, Blob picture) {
        super(id, picture);
    }

    public Picture(Blob picture) {
        super(null, picture);
    }
}
