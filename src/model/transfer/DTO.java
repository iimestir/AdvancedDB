package model.transfer;

public abstract class DTO<T> {
    protected T id;

    public boolean isStored() {
        return (id != null);
    }

    public T getId() {
        return id;
    }

    @Override
    public boolean equals(Object dto) {
        if(dto == null || id == null || dto.getClass() != getClass() || ((DTO)dto).getId().equals(getId()))
            return false;

        return ((DTO) dto).getId().equals(getId());
    }

    @Override
    public int hashCode() {
        int hash = 7;

        return 73 * hash + (this.id != null ? this.id.hashCode() : 0);
    }
}
