package se.citerus.dddsample.location;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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

  // TODO We use reflection to find the location (we wouldn't easily know about the function above without human intervention)
  // We could rewrite this so that reflection is not required (adding the constants in a map in a constructor one by one and then making a find function on that? might be better ways), but for now this is the easiest solution.
  public static Location findConstant (String name) {
    Class<SampleLocations> c = SampleLocations.class;
    for (Field field : c.getDeclaredFields()) {
      int mod = field.getModifiers();
      if (Modifier.isStatic(mod) && Modifier.isPublic(mod) && Modifier.isFinal(mod)) {
        try {
          if (field.getName().equals(name)) { // Case sensitive (we might have two constants with same name but different capitalization)
            return (Location) field.get(null);
          }
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
    throw new IllegalArgumentException(); // We do not expect constants to not be found as we should have found every usage in the calling functions
  }
}
