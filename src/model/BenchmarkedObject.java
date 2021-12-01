package model;

import java.util.Date;

public class BenchmarkedObject<T> {
    private T object;
    private Long time0;
    private Long time1;

    public BenchmarkedObject() {
        this.object = null;
    }

    public void start() {
        time0 = new Date().getTime();
    }

    public void stopAndSaveObject(T object) {
        time1 = new Date().getTime();
        this.object = object;
    }

    public long getOperationDuration() {
        if(time0 == null || time1 == null)
            throw new IllegalStateException("Please use 'start' and 'stop' before calling this method");

        return time1 - time0;
    }

    public T getObject() {
        if(object == null)
            throw new IllegalStateException("Cannot retrieve a void-type object");

        return object;
    }
}