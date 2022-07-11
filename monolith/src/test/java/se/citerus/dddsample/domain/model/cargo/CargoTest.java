package se.citerus.dddsample.domain.model.cargo;

import static org.assertj.core.api.Assertions.assertThat;
import static se.citerus.dddsample.domain.model.cargo.RoutingStatus.MISROUTED;
import static se.citerus.dddsample.domain.model.cargo.RoutingStatus.NOT_ROUTED;
import static se.citerus.dddsample.domain.model.cargo.RoutingStatus.ROUTED;
import static se.citerus.dddsample.domain.model.cargo.TransportStatus.NOT_RECEIVED;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import se.citerus.dddsample.client.Location;
import se.citerus.dddsample.client.LocationClient;
import org.junit.Before;
import org.junit.Test;

import se.citerus.dddsample.application.util.DateTestUtil;
import se.citerus.dddsample.domain.model.handling.HandlingEvent;
import se.citerus.dddsample.domain.model.handling.HandlingHistory;
import se.citerus.dddsample.domain.model.voyage.Voyage;
import se.citerus.dddsample.domain.model.voyage.VoyageNumber;

public class CargoTest {

  private List<HandlingEvent> events;
  private Voyage voyage;

  @Before
  public void setUp() {
    events = new ArrayList<HandlingEvent>();

    voyage = new Voyage.Builder(new VoyageNumber("0123"), LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName()).
      addMovement(LocationClient.sampleLocationsGetLocation("HAMBURG").getName(), new Date(), new Date()).
      addMovement(LocationClient.sampleLocationsGetLocation("HONGKONG").getName(), new Date(), new Date()).
      addMovement(LocationClient.sampleLocationsGetLocation("MELBOURNE").getName(), new Date(), new Date()).
      build();
  }

  @Test
  public void testConstruction() {
    final TrackingId trackingId = new TrackingId("XYZ");
    final Date arrivalDeadline = DateTestUtil.toDate("2009-03-13");
    final RouteSpecification routeSpecification = new RouteSpecification(
      LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName(), LocationClient.sampleLocationsGetLocation("MELBOURNE").getName(), arrivalDeadline
    );

    final Cargo cargo = new Cargo(trackingId, routeSpecification);

    assertThat(cargo.delivery().routingStatus()).isEqualTo(NOT_ROUTED);
    assertThat(cargo.delivery().transportStatus()).isEqualTo(NOT_RECEIVED);
    assertThat(cargo.delivery().lastKnownLocation()).isEqualTo(LocationClient.sampleLocationsGetLocation("UNKNOWN"));
    assertThat(cargo.delivery().currentVoyage()).isEqualTo(Voyage.NONE);    
  }

  @Test
  public void testRoutingStatus() {
    final Cargo cargo = new Cargo(new TrackingId("XYZ"), new RouteSpecification(LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName(), LocationClient.sampleLocationsGetLocation("MELBOURNE").getName(), new Date()));
    final Itinerary good = new Itinerary();
    final Itinerary bad = new Itinerary();
    final RouteSpecification acceptOnlyGood = new RouteSpecification(cargo.origin(), cargo.routeSpecification().destination(), new Date()) {
      @Override
      public boolean isSatisfiedBy(Itinerary itinerary) {
        return itinerary == good;
      }
    };

    cargo.specifyNewRoute(acceptOnlyGood);

    assertThat(cargo.delivery().routingStatus()).isEqualTo(NOT_ROUTED);
    
    cargo.assignToRoute(bad);
    assertThat(cargo.delivery().routingStatus()).isEqualTo(MISROUTED);

    cargo.assignToRoute(good);
    assertThat(cargo.delivery().routingStatus()).isEqualTo(ROUTED);
  }

  @Test
  public void testlastKnownLocationUnknownWhenNoEvents() {
    Cargo cargo = new Cargo(new TrackingId("XYZ"), new RouteSpecification(LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName(), LocationClient.sampleLocationsGetLocation("MELBOURNE").getName(), new Date()));

    assertThat(cargo.delivery().lastKnownLocation()).isEqualTo(LocationClient.sampleLocationsGetLocation("UNKNOWN").getName());
  }

  @Test
  public void testlastKnownLocationReceived() throws Exception {
    Cargo cargo = populateCargoReceivedStockholm();

    assertThat(cargo.delivery().lastKnownLocation()).isEqualTo(LocationClient.sampleLocationsGetLocation("STOCKHOLM"));
  }

  @Test
  public void testlastKnownLocationClaimed() throws Exception {
    Cargo cargo = populateCargoClaimedMelbourne();

    assertThat(cargo.delivery().lastKnownLocation()).isEqualTo(LocationClient.sampleLocationsGetLocation("MELBOURNE"));
  }

  @Test
  public void testlastKnownLocationUnloaded() throws Exception {
    Cargo cargo = populateCargoOffHongKong();

    assertThat(cargo.delivery().lastKnownLocation()).isEqualTo(LocationClient.sampleLocationsGetLocation("HONGKONG"));
  }

  @Test
  public void testlastKnownLocationloaded() throws Exception {
    Cargo cargo = populateCargoOnHamburg();

    assertThat(cargo.delivery().lastKnownLocation()).isEqualTo(LocationClient.sampleLocationsGetLocation("HAMBURG"));
  }

  @Test
  public void testEquality() {
    RouteSpecification spec1 = new RouteSpecification(LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName(), LocationClient.sampleLocationsGetLocation("HONGKONG").getName(), new Date());
    RouteSpecification spec2 = new RouteSpecification(LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName(), LocationClient.sampleLocationsGetLocation("MELBOURNE").getName(), new Date());
    Cargo c1 = new Cargo(new TrackingId("ABC"), spec1);
    Cargo c2 = new Cargo(new TrackingId("CBA"), spec1);
    Cargo c3 = new Cargo(new TrackingId("ABC"), spec2);
    Cargo c4 = new Cargo(new TrackingId("ABC"), spec1);

    assertThat(c1.equals(c4)).as("Cargos should be equal when TrackingIDs are equal").isTrue();
    assertThat(c1.equals(c3)).as("Cargos should be equal when TrackingIDs are equal").isTrue();
    assertThat(c3.equals(c4)).as("Cargos should be equal when TrackingIDs are equal").isTrue();
    assertThat(c1.equals(c2)).as("Cargos are not equal when TrackingID differ").isFalse();
  }

  @Test
  public void testIsUnloadedAtFinalDestination() {
    Cargo cargo = setUpCargoWithItinerary(LocationClient.sampleLocationsGetLocation("HANGZOU").getName(), LocationClient.sampleLocationsGetLocation("TOKYO").getName(), LocationClient.sampleLocationsGetLocation("NEWYORK").getName());
    assertThat(cargo.delivery().isUnloadedAtDestination()).isFalse();

    // Adding an event unrelated to unloading at final destination
    events.add(
      new HandlingEvent(cargo, new Date(10), new Date(), HandlingEvent.Type.RECEIVE, LocationClient.sampleLocationsGetLocation("HANGZOU").getName()));
    cargo.deriveDeliveryProgress(new HandlingHistory(events));
    assertThat(cargo.delivery().isUnloadedAtDestination()).isFalse();

    Voyage voyage = new Voyage.Builder(new VoyageNumber("0123"), LocationClient.sampleLocationsGetLocation("HANGZOU").getName()).
      addMovement(LocationClient.sampleLocationsGetLocation("NEWYORK").getName(), new Date(), new Date()).
      build();

    // Adding an unload event, but not at the final destination
    events.add(
      new HandlingEvent(cargo, new Date(20), new Date(), HandlingEvent.Type.UNLOAD, LocationClient.sampleLocationsGetLocation("TOKYO").getName(), voyage));
    cargo.deriveDeliveryProgress(new HandlingHistory(events));
    assertThat(cargo.delivery().isUnloadedAtDestination()).isFalse();

    // Adding an event in the final destination, but not unload
    events.add(
      new HandlingEvent(cargo, new Date(30), new Date(), HandlingEvent.Type.CUSTOMS, LocationClient.sampleLocationsGetLocation("NEWYORK").getName()));
    cargo.deriveDeliveryProgress(new HandlingHistory(events));
    assertThat(cargo.delivery().isUnloadedAtDestination()).isFalse();

    // Finally, cargo is unloaded at final destination
    events.add(
      new HandlingEvent(cargo, new Date(40), new Date(), HandlingEvent.Type.UNLOAD, LocationClient.sampleLocationsGetLocation("NEWYORK").getName(), voyage));
    cargo.deriveDeliveryProgress(new HandlingHistory(events));
    assertThat(cargo.delivery().isUnloadedAtDestination()).isTrue();
  }

  // TODO: Generate test data some better way
  private Cargo populateCargoReceivedStockholm() throws Exception {
    final Cargo cargo = new Cargo(new TrackingId("XYZ"), new RouteSpecification(LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName(), LocationClient.sampleLocationsGetLocation("MELBOURNE").getName(), new Date()));

    HandlingEvent he = new HandlingEvent(cargo, getDate("2007-12-01"), new Date(), HandlingEvent.Type.RECEIVE, LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName());
    events.add(he);
    cargo.deriveDeliveryProgress(new HandlingHistory(events));

    return cargo;
  }

  private Cargo populateCargoClaimedMelbourne() throws Exception {
    final Cargo cargo = populateCargoOffMelbourne();

    events.add(new HandlingEvent(cargo, getDate("2007-12-09"), new Date(), HandlingEvent.Type.CLAIM, LocationClient.sampleLocationsGetLocation("MELBOURNE").getName()));
    cargo.deriveDeliveryProgress(new HandlingHistory(events));

    return cargo;
  }

  private Cargo populateCargoOffHongKong() throws Exception {
    final Cargo cargo = new Cargo(new TrackingId("XYZ"), new RouteSpecification(LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName(), LocationClient.sampleLocationsGetLocation("MELBOURNE").getName(), new Date()));


    events.add(new HandlingEvent(cargo, getDate("2007-12-01"), new Date(), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName(), voyage));
    events.add(new HandlingEvent(cargo, getDate("2007-12-02"), new Date(), HandlingEvent.Type.UNLOAD, LocationClient.sampleLocationsGetLocation("HAMBURG").getName(), voyage));

    events.add(new HandlingEvent(cargo, getDate("2007-12-03"), new Date(), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("HAMBURG").getName(), voyage));
    events.add(new HandlingEvent(cargo, getDate("2007-12-04"), new Date(), HandlingEvent.Type.UNLOAD, LocationClient.sampleLocationsGetLocation("HONGKONG").getName(), voyage));

    cargo.deriveDeliveryProgress(new HandlingHistory(events));
    return cargo;
  }

  private Cargo populateCargoOnHamburg() throws Exception {
    final Cargo cargo = new Cargo(new TrackingId("XYZ"), new RouteSpecification(LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName(), LocationClient.sampleLocationsGetLocation("MELBOURNE").getName(), new Date()));

    events.add(new HandlingEvent(cargo, getDate("2007-12-01"), new Date(), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName(), voyage));
    events.add(new HandlingEvent(cargo, getDate("2007-12-02"), new Date(), HandlingEvent.Type.UNLOAD, LocationClient.sampleLocationsGetLocation("HAMBURG").getName(), voyage));
    events.add(new HandlingEvent(cargo, getDate("2007-12-03"), new Date(), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("HAMBURG").getName(), voyage));

    cargo.deriveDeliveryProgress(new HandlingHistory(events));
    return cargo;
  }

  private Cargo populateCargoOffMelbourne() throws Exception {
    final Cargo cargo = new Cargo(new TrackingId("XYZ"), new RouteSpecification(LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName(), LocationClient.sampleLocationsGetLocation("MELBOURNE").getName(), new Date()));

    events.add(new HandlingEvent(cargo, getDate("2007-12-01"), new Date(), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName(), voyage));
    events.add(new HandlingEvent(cargo, getDate("2007-12-02"), new Date(), HandlingEvent.Type.UNLOAD, LocationClient.sampleLocationsGetLocation("HAMBURG").getName(), voyage));

    events.add(new HandlingEvent(cargo, getDate("2007-12-03"), new Date(), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("HAMBURG").getName(), voyage));
    events.add(new HandlingEvent(cargo, getDate("2007-12-04"), new Date(), HandlingEvent.Type.UNLOAD, LocationClient.sampleLocationsGetLocation("HONGKONG").getName(), voyage));

    events.add(new HandlingEvent(cargo, getDate("2007-12-05"), new Date(), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("HONGKONG").getName(), voyage));
    events.add(new HandlingEvent(cargo, getDate("2007-12-07"), new Date(), HandlingEvent.Type.UNLOAD, LocationClient.sampleLocationsGetLocation("MELBOURNE").getName(), voyage));

    cargo.deriveDeliveryProgress(new HandlingHistory(events));
    return cargo;
  }

  private Cargo populateCargoOnHongKong() throws Exception {
    final Cargo cargo = new Cargo(new TrackingId("XYZ"), new RouteSpecification(LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName(), LocationClient.sampleLocationsGetLocation("MELBOURNE").getName(), new Date()));

    events.add(new HandlingEvent(cargo, getDate("2007-12-01"), new Date(), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("STOCKHOLM").getName(), voyage));
    events.add(new HandlingEvent(cargo, getDate("2007-12-02"), new Date(), HandlingEvent.Type.UNLOAD, LocationClient.sampleLocationsGetLocation("HAMBURG").getName(), voyage));

    events.add(new HandlingEvent(cargo, getDate("2007-12-03"), new Date(), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("HAMBURG").getName(), voyage));
    events.add(new HandlingEvent(cargo, getDate("2007-12-04"), new Date(), HandlingEvent.Type.UNLOAD, LocationClient.sampleLocationsGetLocation("HONGKONG").getName(), voyage));

    events.add(new HandlingEvent(cargo, getDate("2007-12-05"), new Date(), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("HONGKONG").getName(), voyage));

    cargo.deriveDeliveryProgress(new HandlingHistory(events));
    return cargo;
  }

  @Test
  public void testIsMisdirected() {
    //A cargo with no itinerary is not misdirected
    Cargo cargo = new Cargo(new TrackingId("TRKID"), new RouteSpecification(LocationClient.sampleLocationsGetLocation("SHANGHAI").getName(), LocationClient.sampleLocationsGetLocation("GOTHENBURG").getName(), new Date()));
    assertThat(cargo.delivery().isMisdirected()).isFalse();

    cargo = setUpCargoWithItinerary(LocationClient.sampleLocationsGetLocation("SHANGHAI").getName(), LocationClient.sampleLocationsGetLocation("ROTTERDAM").getName(), LocationClient.sampleLocationsGetLocation("GOTHENBURG").getName());

    //A cargo with no handling events is not misdirected
    assertThat(cargo.delivery().isMisdirected()).isFalse();

    Collection<HandlingEvent> handlingEvents = new ArrayList<HandlingEvent>();

    //Happy path
    handlingEvents.add(new HandlingEvent(cargo, new Date(10), new Date(20), HandlingEvent.Type.RECEIVE, LocationClient.sampleLocationsGetLocation("SHANGHAI").getName()));
    handlingEvents.add(new HandlingEvent(cargo, new Date(30), new Date(40), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("SHANGHAI").getName(), voyage));
    handlingEvents.add(new HandlingEvent(cargo, new Date(50), new Date(60), HandlingEvent.Type.UNLOAD, LocationClient.sampleLocationsGetLocation("ROTTERDAM").getName(), voyage));
    handlingEvents.add(new HandlingEvent(cargo, new Date(70), new Date(80), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("ROTTERDAM").getName(), voyage));
    handlingEvents.add(new HandlingEvent(cargo, new Date(90), new Date(100), HandlingEvent.Type.UNLOAD, LocationClient.sampleLocationsGetLocation("GOTHENBURG").getName(), voyage));
    handlingEvents.add(new HandlingEvent(cargo, new Date(110), new Date(120), HandlingEvent.Type.CLAIM, LocationClient.sampleLocationsGetLocation("GOTHENBURG").getName()));
    handlingEvents.add(new HandlingEvent(cargo, new Date(130), new Date(140), HandlingEvent.Type.CUSTOMS, LocationClient.sampleLocationsGetLocation("GOTHENBURG").getName()));

    events.addAll(handlingEvents);
    cargo.deriveDeliveryProgress(new HandlingHistory(events));
    assertThat(cargo.delivery().isMisdirected()).isFalse();

    //Try a couple of failing ones

    cargo = setUpCargoWithItinerary(LocationClient.sampleLocationsGetLocation("SHANGHAI").getName(), LocationClient.sampleLocationsGetLocation("ROTTERDAM").getName(), LocationClient.sampleLocationsGetLocation("GOTHENBURG").getName());
    handlingEvents = new ArrayList<HandlingEvent>();

    handlingEvents.add(new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.RECEIVE, LocationClient.sampleLocationsGetLocation("HANGZOU").getName()));
    events.addAll(handlingEvents);
    cargo.deriveDeliveryProgress(new HandlingHistory(events));

    assertThat(cargo.delivery().isMisdirected()).isTrue();


    cargo = setUpCargoWithItinerary(LocationClient.sampleLocationsGetLocation("SHANGHAI").getName(), LocationClient.sampleLocationsGetLocation("ROTTERDAM").getName(), LocationClient.sampleLocationsGetLocation("GOTHENBURG").getName());
    handlingEvents = new ArrayList<HandlingEvent>();

    handlingEvents.add(new HandlingEvent(cargo, new Date(10), new Date(20), HandlingEvent.Type.RECEIVE, LocationClient.sampleLocationsGetLocation("SHANGHAI").getName()));
    handlingEvents.add(new HandlingEvent(cargo, new Date(30), new Date(40), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("SHANGHAI").getName(), voyage));
    handlingEvents.add(new HandlingEvent(cargo, new Date(50), new Date(60), HandlingEvent.Type.UNLOAD, LocationClient.sampleLocationsGetLocation("ROTTERDAM").getName(), voyage));
    handlingEvents.add(new HandlingEvent(cargo, new Date(70), new Date(80), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("ROTTERDAM").getName(), voyage));

    events.addAll(handlingEvents);
    cargo.deriveDeliveryProgress(new HandlingHistory(events));

    assertThat(cargo.delivery().isMisdirected()).isTrue();


    cargo = setUpCargoWithItinerary(LocationClient.sampleLocationsGetLocation("SHANGHAI").getName(), LocationClient.sampleLocationsGetLocation("ROTTERDAM").getName(), LocationClient.sampleLocationsGetLocation("GOTHENBURG").getName());
    handlingEvents = new ArrayList<HandlingEvent>();

    handlingEvents.add(new HandlingEvent(cargo, new Date(10), new Date(20), HandlingEvent.Type.RECEIVE, LocationClient.sampleLocationsGetLocation("SHANGHAI").getName()));
    handlingEvents.add(new HandlingEvent(cargo, new Date(30), new Date(40), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("SHANGHAI").getName(), voyage));
    handlingEvents.add(new HandlingEvent(cargo, new Date(50), new Date(60), HandlingEvent.Type.UNLOAD, LocationClient.sampleLocationsGetLocation("ROTTERDAM").getName(), voyage));
    handlingEvents.add(new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.CLAIM, LocationClient.sampleLocationsGetLocation("ROTTERDAM").getName()));

    events.addAll(handlingEvents);
    cargo.deriveDeliveryProgress(new HandlingHistory(events));

    assertThat(cargo.delivery().isMisdirected()).isTrue();
  }

  private Cargo setUpCargoWithItinerary(String origin, String midpoint, String destination) {
    Cargo cargo = new Cargo(new TrackingId("CARGO1"), new RouteSpecification(origin, destination, new Date()));

    Itinerary itinerary = new Itinerary(
      Arrays.asList(
        new Leg(voyage, origin, midpoint, new Date(), new Date()),
        new Leg(voyage, midpoint, destination, new Date(), new Date())
      )
    );

    cargo.assignToRoute(itinerary);
    return cargo;
  }

  /**
   * Parse an ISO 8601 (YYYY-MM-DD) String to Date
   *
   * @param isoFormat String to parse.
   * @return Created date instance.
   * @throws ParseException Thrown if parsing fails.
   */
  private Date getDate(String isoFormat) throws ParseException {
    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return dateFormat.parse(isoFormat);
  }
}
