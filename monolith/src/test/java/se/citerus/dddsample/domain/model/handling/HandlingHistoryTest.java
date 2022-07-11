package se.citerus.dddsample.domain.model.handling;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static se.citerus.dddsample.application.util.DateTestUtil.toDate;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import se.citerus.dddsample.client.LocationClient;
import se.citerus.dddsample.domain.model.cargo.Cargo;
import se.citerus.dddsample.domain.model.cargo.RouteSpecification;
import se.citerus.dddsample.domain.model.cargo.TrackingId;
import se.citerus.dddsample.domain.model.voyage.Voyage;
import se.citerus.dddsample.domain.model.voyage.VoyageNumber;

public class HandlingHistoryTest {
  Cargo cargo;
  Voyage voyage;
  HandlingEvent event1;
  HandlingEvent event1duplicate;
  HandlingEvent event2;
  HandlingHistory handlingHistory;

  @Before
  public void setUp() {
    cargo = new Cargo(new TrackingId("ABC"), new RouteSpecification(LocationClient.sampleLocationsGetLocation("SHANGHAI").getName(), LocationClient.sampleLocationsGetLocation("DALLAS").getName(), toDate("2009-04-01")));
    voyage = new Voyage.Builder(new VoyageNumber("X25"), LocationClient.sampleLocationsGetLocation("HONGKONG").getName()).
      addMovement(LocationClient.sampleLocationsGetLocation("SHANGHAI").getName(), new Date(), new Date()).
      addMovement(LocationClient.sampleLocationsGetLocation("DALLAS").getName(), new Date(), new Date()).
      build();
    event1 = new HandlingEvent(cargo, toDate("2009-03-05"), new Date(100), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("SHANGHAI").getName(), voyage);
    event1duplicate = new HandlingEvent(cargo, toDate("2009-03-05"), new Date(200), HandlingEvent.Type.LOAD, LocationClient.sampleLocationsGetLocation("SHANGHAI").getName(), voyage);
    event2 = new HandlingEvent(cargo, toDate("2009-03-10"), new Date(150), HandlingEvent.Type.UNLOAD, LocationClient.sampleLocationsGetLocation("DALLAS").getName(), voyage);

    handlingHistory = new HandlingHistory(asList(event2, event1, event1duplicate));
  }

  @Test
  public void testDistinctEventsByCompletionTime() {
    assertThat(handlingHistory.distinctEventsByCompletionTime()).isEqualTo(asList(event1, event2));
  }

  @Test
  public void testMostRecentlyCompletedEvent() {
    assertThat(handlingHistory.mostRecentlyCompletedEvent()).isEqualTo(event2);
  }
  
}
