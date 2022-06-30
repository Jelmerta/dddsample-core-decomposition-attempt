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
    public static CreateLocationResponse createLocation(UnLocode unLocode, String name) {
        CreateLocationRequest request = CreateLocationRequest.newBuilder()
                .setUnLocode(unLocode)
                .setName(name)
                .build();
        return stub.createLocation(request);
    }

    public static CreateUnLocodeResponse createUnLocode(String unlocode) {
        CreateUnLocodeRequest request = CreateUnLocodeRequest.newBuilder()
                .setUnlocode(unlocode)
                .build();
        return stub.createUnLocode(request);
    }

    public static List<Location> SampleLocationsGetAll() {
        SampleLocationsGetAllRequest request = SampleLocationsGetAllRequest.newBuilder()
                .build();

        // TODO Return response or the list...?
        return stub.sampleLocationsGetAll(request).getLocationsList();
    }
}
