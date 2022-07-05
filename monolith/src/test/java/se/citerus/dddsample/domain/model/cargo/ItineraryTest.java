package se.citerus.dddsample.domain.model.cargo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import se.citerus.dddsample.client.LocationClient;
import se.citerus.dddsample.domain.model.handling.HandlingEvent;
import se.citerus.dddsample.domain.model.voyage.CarrierMovement;
import se.citerus.dddsample.domain.model.voyage.Voyage;
import se.citerus.dddsample.domain.model.voyage.VoyageNumber;

public class ItineraryTest {
  private final CarrierMovement abc = new CarrierMovement(LocationClient.sampleLocationsGetLocation("SHANGHAI"), LocationClient.sampleLocationsGetLocation("ROTTERDAM"), new Date(), new Date());
  private final CarrierMovement def = new CarrierMovement(LocationClient.sampleLocationsGetLocation("ROTTERDAM"), LocationClient.sampleLocationsGetLocation("GOTHENBURG"), new Date(), new Date());
  private final CarrierMovement ghi = new CarrierMovement(LocationClient.sampleLocationsGetLocation("ROTTERDAM"), LocationClient.sampleLocationsGetLocation("NEWYORK"), new Date(), new Date());
  private final CarrierMovement jkl = new CarrierMovement(LocationClient.sampleLocationsGetLocation("SHANGHAI"), LocationClient.sampleLocationsGetLocation("HELSINKI"), new Date(), new Date());

  Voyage voyage, wrongVoyage;

  @Before
  public void setUp() {
    voyage = new Voyage.Builder(new VoyageNumber("0123"), LocationClient.sampleLocationsGetLocation("SHANGHAI")).
      addMovement(LocationClient.sampleLocationsGetLocation("ROTTERDAM"), new Date(), new Date()).
      addMovement(LocationClient.sampleLocationsGetLocation("GOTHENBURG"), new Date(), new Date()).
      build();

    wrongVoyage = new Voyage.Builder(new VoyageNumber("666"), LocationClient.sampleLocationsGetLocation("NEWYORK")).
      addMovement(LocationClient.sampleLocationsGetLocation("STOCKHOLM"), new Date(), new Date()).
      addMovement(LocationClient.sampleLocationsGetLocation("HELSINKI"), new Date(), new Date()).
      build();
  }

  @Test
  public void testCargoOnTrack() {

    TrackingId trackingId = new TrackingId("CARGO1");
    RouteSpecification routeSpecification = new RouteSpecification(LocationClient.sampleLocationsGetLocation("SHANGHAI"), LocationClient.sampleLocationsGetLocation("GOTHENBURG"), new Date());
    Cargo cargo = new Cargo(trackingId, routeSpecification);

    Itinerary itinerary = new Itinerary(
      Arrays.asList(
        new Leg(voyage, LocationClient.sampleLocationsGetLocation("SHANGHAI"), LocationClient.sampleLocationsGetLocation("ROTTERDAM"), new Date(), new Date()),
        new Leg(voyage, LocationClient.sampleLocationsGetLocation("ROTTERDAM"), LocationClient.sampleLocationsGetLocation("GOTHENBURG"), new Date(), new Date())
      )
    );

    //Happy path
    HandlingEvent event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.RECEIVE, LocationClient.sampleLocationsGetLocation("SHANGHAI"));
    assertThat(itinerary.isExpected(event)).isTrue();

    event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("SHANGHAI"), voyage);
    assertThat(itinerary.isExpected(event)).isTrue();

    event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.UNLOAD, LocationClient.sampleLocationsGetLocation("ROTTERDAM"), voyage);
    assertThat(itinerary.isExpected(event)).isTrue();

    event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("ROTTERDAM"), voyage);
    assertThat(itinerary.isExpected(event)).isTrue();

    event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.UNLOAD, LocationClient.sampleLocationsGetLocation("GOTHENBURG"), voyage);
    assertThat(itinerary.isExpected(event)).isTrue();

    event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.CLAIM, LocationClient.sampleLocationsGetLocation("GOTHENBURG"));
    assertThat(itinerary.isExpected(event)).isTrue();

    //Customs event changes nothing
    event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.CUSTOMS, LocationClient.sampleLocationsGetLocation("GOTHENBURG"));
    assertThat(itinerary.isExpected(event)).isTrue();

    //Received at the wrong location
    event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.RECEIVE, LocationClient.sampleLocationsGetLocation("HANGZOU"));
    assertThat(itinerary.isExpected(event)).isFalse();

    //Loaded to onto the wrong ship, correct location
    event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("ROTTERDAM"), wrongVoyage);
    assertThat(itinerary.isExpected(event)).isFalse();

    //Unloaded from the wrong ship in the wrong location
    event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.UNLOAD, LocationClient.sampleLocationsGetLocation("HELSINKI"), wrongVoyage);
    assertThat(itinerary.isExpected(event)).isFalse();

    event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.CLAIM, LocationClient.sampleLocationsGetLocation("ROTTERDAM"));
    assertThat(itinerary.isExpected(event)).isFalse();

  }
  @Test
  public void testNextExpectedEvent() {

  }
  @Test
  public void testCreateItinerary() {
    try {
      new Itinerary(new ArrayList<>());
      fail("An empty itinerary is not OK");
    } catch (IllegalArgumentException iae) {
      //Expected
    }

    try {
      List<Leg> legs = null;
      new Itinerary(legs);
      fail("Null itinerary is not OK");
    } catch (IllegalArgumentException iae) {
      //Expected
    }
  }

}