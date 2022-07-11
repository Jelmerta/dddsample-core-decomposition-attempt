package se.citerus.dddsample.location;

import io.grpc.stub.StreamObserver;
import se.citerus.dddsample.client.*;

import java.util.List;
import java.util.stream.Collectors;

// TODO You probably want to use this function to also store the location in a database/data structure in this service.
// It would be hard to detect when to replace that.
// Perhaps replacing new with this call is not the best option, though I don't know a better option.
public class LocationServiceImpl extends LocationServiceGrpc.LocationServiceImplBase {
    @Override
    public void createLocation(CreateLocationRequest request, StreamObserver<CreateLocationResponse> responseObserver) {
        // TODO Unlocode has already been created and is now passed again to constructor
        // TODO Constructor performs checks, making the previously created unlocode with different code base possibly invalid. Do we just want to create a data object and therefore add a constructor from proto POJO to original POJO?
        // Or is this just how this code base functions, and should I not pay much mind to that?
        UnLocode unLocode = new UnLocode(request.getUnLocode().getUnlocode());
        Location location = new Location(unLocode, request.getName());
        // TODO We need some POJO to proto structure converter.

        se.citerus.dddsample.client.UnLocode unLocodeProto = se.citerus.dddsample.client.UnLocode.newBuilder()
                .setUnlocode(unLocode.getUnlocode())
                .build();
        se.citerus.dddsample.client.Location locationProto = se.citerus.dddsample.client.Location.newBuilder()
                .setUnLocode(unLocodeProto)
                .setName(location.name())
                .build();

        CreateLocationResponse response = CreateLocationResponse.newBuilder()
                .setLocation(locationProto)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createUnLocode(CreateUnLocodeRequest request, StreamObserver<CreateUnLocodeResponse> responseObserver) {
        UnLocode unLocode = new UnLocode(request.getUnlocode());
        // TODO We need some POJO to proto structure converter?

        se.citerus.dddsample.client.UnLocode unLocodeProto = se.citerus.dddsample.client.UnLocode.newBuilder()
                .setUnlocode(unLocode.getUnlocode())
                .build();

        CreateUnLocodeResponse response = CreateUnLocodeResponse.newBuilder()
                .setUnLocode(unLocodeProto)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void sampleLocationsGetLocation(SampleLocationsGetLocationRequest request, StreamObserver<SampleLocationsGetLocationResponse> responseObserver) {
        Location location = SampleLocations.findConstant(request.getName());
        se.citerus.dddsample.client.UnLocode unLocodeProto = se.citerus.dddsample.client.UnLocode.newBuilder()
                .setUnlocode(location.unLocode().getUnlocode())
                .build();

        se.citerus.dddsample.client.Location locationProto = se.citerus.dddsample.client.Location.newBuilder()
                .setName(location.name())
                .setUnLocode(unLocodeProto)
                .build();
        // TODO We need some POJO to proto structure converter?

        SampleLocationsGetLocationResponse response = SampleLocationsGetLocationResponse.newBuilder()
                .setLocation(locationProto)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void locationSameIdentityAs(LocationSameIdentityAsRequest request, StreamObserver<LocationSameIdentityAsResponse> responseObserver) {
        // TODO Maybe instead of new you would expect the data to be retrieved. If it does not exist you might want to deal with it by throwing an error, or by generating the data on the fly...?
        // Maybe make it so developer has the option to implement a getLocation from a database. Unfeasible in most cases to do this?

        Location thisLocation = SampleLocations.findConstant(request.getThisLocation());
        Location location = SampleLocations.findConstant(request.getLocation());
//        Location location = new Location(request.getLocation());
        boolean sameIdentityAs = thisLocation.sameIdentityAs(location);
        // TODO We need some POJO to proto structure converter?

        LocationSameIdentityAsResponse response = LocationSameIdentityAsResponse.newBuilder()
                .setBoolean(sameIdentityAs)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void sampleLocationsGetAll(SampleLocationsGetAllRequest request, StreamObserver<SampleLocationsGetAllResponse> responseObserver) {
        List<Location> locationList = SampleLocations.getAll();
        List<se.citerus.dddsample.client.Location> locationProtoList = locationList.stream()
                .map(location -> se.citerus.dddsample.client.Location.newBuilder().setUnLocode(se.citerus.dddsample.client.UnLocode.newBuilder().setUnlocode(location.unLocode().getUnlocode()).build()).setName(location.name()).build())
                .collect(Collectors.toList());

        SampleLocationsGetAllResponse response = SampleLocationsGetAllResponse.newBuilder()
                .addAllLocations(locationProtoList)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
