package se.citerus.dddsample.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.List;

public class LocationClient {
            private static ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                    .usePlaintext()
                    .build();
            private static LocationServiceGrpc.LocationServiceBlockingStub stub
                    = LocationServiceGrpc.newBlockingStub(channel);

//            System.out.println(helloResponse.getGreeting());

//            channel.shutdown();

    // TODO Not sure about static, maybe a singleton?
    // TODO createLocation only used in Location service now, so no outside dependency and therefore not required?
    public static Location createLocation(UnLocode unLocode, String name) {
        CreateLocationRequest request = CreateLocationRequest.newBuilder()
                .setUnLocode(unLocode)
                .setName(name)
                .build();
        return stub.createLocation(request).getLocation();
    }

    public static UnLocode createUnLocode(String unlocode) {
        CreateUnLocodeRequest request = CreateUnLocodeRequest.newBuilder()
                .setUnlocode(unlocode)
                .build();
        return stub.createUnLocode(request).getUnLocode();
    }

    public static List<Location> sampleLocationsGetAll() {
        SampleLocationsGetAllRequest request = SampleLocationsGetAllRequest.newBuilder()
                .build();

        // TODO Return response or the list...?
        return stub.sampleLocationsGetAll(request).getLocationsList();
    }

    public static Location sampleLocationsGetLocation(String name) {
        SampleLocationsGetLocationRequest request = SampleLocationsGetLocationRequest.newBuilder()
                .setName(name)
                .build();

        return stub.sampleLocationsGetLocation(request).getLocation();
    }

    // TODO Arguments should just be id of locations, as the location service is aware of what locations you are asking about
    public static boolean locationSameIdentityAs(String thisLocationId, String locationId) {
        LocationSameIdentityAsRequest request = LocationSameIdentityAsRequest.newBuilder()
                .setThisLocation(thisLocationId)
                .setLocation(locationId)
                .build();

        return stub.locationSameIdentityAs(request).getBoolean();
    }
}
