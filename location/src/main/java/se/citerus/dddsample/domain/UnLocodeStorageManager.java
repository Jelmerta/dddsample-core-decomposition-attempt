package se.citerus.dddsample.domain;

import java.util.HashMap;
import java.util.UUID;

// TODO Should objects contain their own ids?
// TODO Uniqueness?
public class UnLocodeStorageManager {
    public static HashMap<String, UnLocode> unLocodeHashMap = new HashMap<>();

    static {
        // Load initial data. Copy of SampleLocations. This is so the reference id in hibernate matches with that in the location service at startup.
        // TODO Probably not something we can automate easily? Need to make a note that sayd that startup data loaded dynamically, not through the new service, needs to be duplicated or an alternative needs to be used.

        unLocodeHashMap.put("1", new UnLocode("SESTO"));
        unLocodeHashMap.put("2", new UnLocode("AUMEL"));
        unLocodeHashMap.put("3", new UnLocode("CNHKG"));
        unLocodeHashMap.put("4", new UnLocode("JPTOK"));
        unLocodeHashMap.put("5", new UnLocode("FIHEL"));
        unLocodeHashMap.put("6", new UnLocode("DEHAM"));
        unLocodeHashMap.put("7", new UnLocode("USCHI"));
    }

    public static UnLocode getUnLocode(String unLocodeId) {
        return unLocodeHashMap.get(unLocodeId);
    }

    public static String addUnLocode(UnLocode unLocode) {
        String id = UUID.randomUUID().toString(); // TODO There might be a better way to generate.
        unLocodeHashMap.put(id, unLocode);
        return id;
    }
}
