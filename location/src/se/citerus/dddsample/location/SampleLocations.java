package se.citerus.dddsample.location;

import se.citerus.dddsample.client.LocationClient;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sample locations, for test purposes.
 * 
 */
// TODO Suggested to be in Location service... This is test data.
// Refactored, I think this should be data-only code, without dependency on Location service.
// Not too sure what to do with this now...
// I think one way to notice this is to find public static final constants.
// Probably do not want to add these as environment variables either, becomes a mess.
// Maybe these are marked as: "Work needs to be done to make these classes functional"? Refactoring this is probably the best way unless we find a solution that developers will be happy with.
// Perhaps an option is to add these in the proto as well as constants if that is possible? Not really the right location imo as it is test data.
  // Maybe a "SampleLocations.getStatic(String)"? Potentially different return types in other cases...
  // Worst case: Different call for every variable. Would be easier to deal with probably...
public class SampleLocations {

  public static final Location HONGKONG = new Location(new UnLocode("CNHKG"), "Hongkong");
  public static final Location MELBOURNE = new Location(new UnLocode("AUMEL"), "Melbourne");
  public static final Location STOCKHOLM = new Location(new UnLocode("SESTO"), "Stockholm");
  public static final Location HELSINKI = new Location(new UnLocode("FIHEL"), "Helsinki");
  public static final Location CHICAGO = new Location(new UnLocode("USCHI"), "Chicago");
  public static final Location TOKYO = new Location(new UnLocode("JNTKO"), "Tokyo");
  public static final Location HAMBURG = new Location(new UnLocode("DEHAM"), "Hamburg");
  public static final Location SHANGHAI = new Location(new UnLocode("CNSHA"), "Shanghai");
  public static final Location ROTTERDAM = new Location(new UnLocode("NLRTM"), "Rotterdam");
  public static final Location GOTHENBURG = new Location(new UnLocode("SEGOT"), "Göteborg");
  public static final Location HANGZOU = new Location(new UnLocode("CNHGH"), "Hangzhou");
  public static final Location NEWYORK = new Location(new UnLocode("USNYC"), "New York");
  public static final Location DALLAS = new Location(new UnLocode("USDAL"), "Dallas");

  public static final Map<UnLocode, Location> ALL = new HashMap<UnLocode, Location>();

  static {
    for (Field field : SampleLocations.class.getDeclaredFields()) {
      if (field.getType().equals(Location.class)) {
        try {
          Location location = (Location) field.get(null);
          ALL.put(location.unLocode(), location);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  // TODO Used as service side getAll
  public static List<Location> getAll() {
    return new ArrayList<Location>(ALL.values());
  }

  public static Location lookup(UnLocode unLocode) {
    return ALL.get(unLocode);
  }

}
