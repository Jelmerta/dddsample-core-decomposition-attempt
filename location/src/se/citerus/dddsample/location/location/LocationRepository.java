package se.citerus.dddsample.location.location;

import se.citerus.dddsample.client.Location;
import se.citerus.dddsample.client.UnLocode;

import java.util.List;

// TODO Odd how this one was not found... Still have to deal with that though.
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
