package se.citerus.dddsample.infrastructure.persistence.inmemory;

import se.citerus.dddsample.client.Location;
import se.citerus.dddsample.client.LocationClient;
import se.citerus.dddsample.client.UnLocode;
import se.citerus.dddsample.domain.model.location.LocationRepository;

import java.util.List;

public class LocationRepositoryInMem implements LocationRepository {

  public Location find(UnLocode unLocode) {
    for (Location location : LocationClient.SampleLocationsGetAll()) {
      if (location.getUnLocode().equals(unLocode)) {
        return location;
      }
    }
    return null;
  }

  public List<Location> findAll() {
    return LocationClient.SampleLocationsGetAll();
  }
  
}
