package se.citerus.dddsample.domain;

import org.apache.commons.lang.Validate;
import se.citerus.dddsample.common.ValueObject;

public class LocationId implements ValueObject<LocationId> {

    private String id;

    public LocationId(String id) {
        Validate.notNull(id);

        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof LocationId)) return false;

        final LocationId other = (LocationId) o;

        return sameValueAs(other);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean sameValueAs(LocationId other) {
        return other != null && this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return id;
    }

    public String idString() {
        return id;
    }

    LocationId() {
        // Needed by Hibernate
    }

}

