package se.citerus.dddsample.location;

import se.citerus.dddsample.client.Location;
import se.citerus.dddsample.client.LocationClient;
import se.citerus.dddsample.client.UnLocode;
import se.citerus.dddsample.location.location.LocationRepository;

import java.util.List;

public class LocationRepositoryInMem implements LocationRepository {

  public Location find(UnLocode unLocode) {
    for (Location location : LocationClient.sampleLocationsGetAll()) {
      if (location.getUnLocode().equals(unLocode)) {
        return location;
      }
    }
    return null;
  }

  public List<Location> findAll() {
    return LocationClient.sampleLocationsGetAll();
  }
  
}
