package se.citerus.dddsample.infrastructure.persistence.inmemory;

import se.citerus.dddsample.client.LocationClient;
import se.citerus.dddsample.domain.model.cargo.Cargo;
import se.citerus.dddsample.domain.model.cargo.CargoRepository;
import se.citerus.dddsample.domain.model.cargo.RouteSpecification;
import se.citerus.dddsample.domain.model.cargo.TrackingId;
import se.citerus.dddsample.domain.model.handling.HandlingEventRepository;
import se.citerus.dddsample.domain.model.handling.HandlingHistory;

import java.util.*;

/**
 * CargoRepositoryInMem implement the CargoRepository interface but is a test
 * class not intended for usage in real application.
 * <p>
 * It setup a simple local hash with a number of Cargo's with TrackingId as key
 * defined at compile time.
 * <p>
 */
public class CargoRepositoryInMem implements CargoRepository {

    private Map<String, Cargo> cargoDb;
    private HandlingEventRepository handlingEventRepository;

    /**
     * Constructor.
     */
    public CargoRepositoryInMem() {
        cargoDb = new HashMap<>();
    }

    public Cargo find(final TrackingId trackingId) {
        return cargoDb.get(trackingId.idString());
    }

    public void store(final Cargo cargo) {
        cargoDb.put(cargo.trackingId().idString(), cargo);
    }

    public TrackingId nextTrackingId() {
        String random = UUID.randomUUID().toString().toUpperCase();
        return new TrackingId(
                random.substring(0, random.indexOf("-"))
        );
    }

    public List<Cargo> findAll() {
        return new ArrayList<>(cargoDb.values());
    }

    public void init() throws Exception {
        final TrackingId xyz = new TrackingId("XYZ");
        final Cargo cargoXYZ = createCargoWithDeliveryHistory(
                xyz, LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName(), LocationClient.sampleLocationsGetLocation("MELBOURNE").getName(), handlingEventRepository.lookupHandlingHistoryOfCargo(xyz));
        cargoDb.put(xyz.idString(), cargoXYZ);

        final TrackingId zyx = new TrackingId("ZYX");
        final Cargo cargoZYX = createCargoWithDeliveryHistory(
                zyx, LocationClient.sampleLocationsGetLocation("MELBOURNE").getName(), LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName(), handlingEventRepository.lookupHandlingHistoryOfCargo(zyx));
        cargoDb.put(zyx.idString(), cargoZYX);

        final TrackingId abc = new TrackingId("ABC");
        final Cargo cargoABC = createCargoWithDeliveryHistory(
                abc, LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName(), LocationClient.sampleLocationsGetLocation("HELSINKI").getName(), handlingEventRepository.lookupHandlingHistoryOfCargo(abc));
        cargoDb.put(abc.idString(), cargoABC);

        final TrackingId cba = new TrackingId("CBA");
        final Cargo cargoCBA = createCargoWithDeliveryHistory(
                cba, LocationClient.sampleLocationsGetLocation("HELSINKI").getName(), LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName(), handlingEventRepository.lookupHandlingHistoryOfCargo(cba));
        cargoDb.put(cba.idString(), cargoCBA);
    }

    public void setHandlingEventRepository(final HandlingEventRepository handlingEventRepository) {
        this.handlingEventRepository = handlingEventRepository;
    }

    public static Cargo createCargoWithDeliveryHistory(TrackingId trackingId,
                                                       String origin,
                                                       String destination,
                                                       HandlingHistory handlingHistory) {

        final RouteSpecification routeSpecification = new RouteSpecification(origin, destination, new Date());
        final Cargo cargo = new Cargo(trackingId, routeSpecification);
        cargo.deriveDeliveryProgress(handlingHistory);

        return cargo;
    }
}
