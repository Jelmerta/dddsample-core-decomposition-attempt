package se.citerus.dddsample.location;

import org.apache.commons.lang.Validate;
import se.citerus.dddsample.client.CreateLocationRequest;
import se.citerus.dddsample.common.Entity;

/**
 * A location is our model is stops on a journey, such as cargo
 * origin or destination, or carrier movement endpoints.
 *
 * It is uniquely identified by a UN Locode.
 *
 */
public final class Location implements Entity<Location> {

  private UnLocode unLocode;
  private String name;

  /**
   * Special Location object that marks an unknown location.
   */
  // TODO We probably shouldn't call client when in own service though... Should we just generate these the old way?
  public static final Location UNKNOWN = new Location(
    new UnLocode("XXXXX"), "Unknown location"
  );

  // TODO Maybe always introduce a constructor from proto to the POJO for deserialization?
  public Location(se.citerus.dddsample.client.Location locationProto) {
    this.unLocode = new UnLocode(locationProto.getUnLocode());
    this.name = locationProto.getName();
  }

  /**
   * Package-level constructor, visible for test only.
   *
   * @param unLocode UN Locode
   * @param name     location name
   * @throws IllegalArgumentException if the UN Locode or name is null
   */
  Location(final UnLocode unLocode, final String name) {
    Validate.notNull(unLocode);
    Validate.notNull(name);
    
    this.unLocode = unLocode;
    this.name = name;
  }

  /**
   * @return UN Locode for this location.
   */
  public UnLocode unLocode() {
    return unLocode;
  }

  /**
   * @return Actual name of this location, e.g. "Stockholm".
   */
  public String name() {
    return name;
  }

  /**
   * @param object to compare
   * @return Since this is an entiy this will be true iff UN locodes are equal.
   */
  @Override
  public boolean equals(final Object object) {
    if (object == null) {
      return false;
    }
    if (this == object) {
      return true;
    }
    if (!(object instanceof Location)) {
      return false;
    }
    Location other = (Location) object;
    return sameIdentityAs(other);
  }

  @Override
  public boolean sameIdentityAs(final Location other) {
    return this.unLocode.sameValueAs(other.unLocode);
  }

  /**
   * @return Hash code of UN locode.
   */
  @Override
  public int hashCode() {
    return unLocode.hashCode();
  }

  @Override
  public String toString() {
    return name + " [" + unLocode + "]";
  }

  Location() {
    // Needed by Hibernate
  }

  private Long id;

}
