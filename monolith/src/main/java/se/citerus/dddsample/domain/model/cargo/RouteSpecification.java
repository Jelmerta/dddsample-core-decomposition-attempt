package se.citerus.dddsample.domain.model.cargo;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import se.citerus.dddsample.client.LocationClient;
import se.citerus.dddsample.domain.LocationId;
import se.citerus.dddsample.domain.shared.AbstractSpecification;
import se.citerus.dddsample.common.ValueObject;

import java.util.Date;

/**
 * Route specification. Describes where a cargo origin and destination is,
 * and the arrival deadline.
 * 
 */
public class RouteSpecification extends AbstractSpecification<Itinerary> implements ValueObject<RouteSpecification> {

  private LocationId origin;
  private LocationId destination;
  private Date arrivalDeadline;

  /**
   * @param origin origin location - can't be the same as the destination
   * @param destination destination location - can't be the same as the origin
   * @param arrivalDeadline arrival deadline
   */
  public RouteSpecification(final LocationId origin, final LocationId destination, final Date arrivalDeadline) {
    Validate.notNull(origin, "Origin is required");
    Validate.notNull(destination, "Destination is required");
    Validate.notNull(arrivalDeadline, "Arrival deadline is required");
    Validate.isTrue(!LocationClient.locationSameIdentityAs(origin.idString(), destination.idString()), "Origin and destination can't be the same: " + origin);

    this.origin = origin;
    this.destination = destination;
    this.arrivalDeadline = (Date) arrivalDeadline.clone();
  }

  /**
   * @return Specified origin location.
   */
  public LocationId origin() {
    return origin;
  }

  /**
   * @return Specfied destination location.
   */
  public LocationId destination() {
    return destination;
  }

  /**
   * @return Arrival deadline.
   */
  public Date arrivalDeadline() {
    return new Date(arrivalDeadline.getTime());
  }

  @Override
  public boolean isSatisfiedBy(final Itinerary itinerary) {
    return itinerary != null &&
           LocationClient.locationSameIdentityAs(origin().idString(), itinerary.initialDepartureLocation().idString()) &&
           LocationClient.locationSameIdentityAs(destination().idString(), itinerary.finalArrivalLocation().idString()) &&
           arrivalDeadline().after(itinerary.finalArrivalDate());
  }

  @Override
  public boolean sameValueAs(final RouteSpecification other) {
    return other != null && new EqualsBuilder().
      append(this.origin, other.origin).
      append(this.destination, other.destination).
      append(this.arrivalDeadline, other.arrivalDeadline).
      isEquals();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final RouteSpecification that = (RouteSpecification) o;

    return sameValueAs(that);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().
      append(this.origin).
      append(this.destination).
      append(this.arrivalDeadline).
      toHashCode();
  }

  RouteSpecification() {
    // Needed by Hibernate
  }
  
}
