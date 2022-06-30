package se.citerus.dddsample.domain.model.location;

import se.citerus.dddsample.location.Location;
import se.citerus.dddsample.location.UnLocode;

import java.util.List;

public interface LocationRepository {

  /**
   * Finds a location using given unlocode.
   *
   * @param unLocode UNLocode.
   * @return Location.
   */
  Location find(UnLocode unLocode);

  /**
   * Finds all locations.
   *
   * @return All locations.
   */
  List<Location> findAll();

}
