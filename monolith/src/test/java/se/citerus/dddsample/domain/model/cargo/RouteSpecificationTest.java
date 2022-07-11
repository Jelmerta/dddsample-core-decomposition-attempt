package se.citerus.dddsample.domain.model.cargo;

import static org.assertj.core.api.Assertions.assertThat;
import static se.citerus.dddsample.application.util.DateTestUtil.toDate;


import java.util.Arrays;

import org.junit.Test;

import se.citerus.dddsample.client.LocationClient;
import se.citerus.dddsample.domain.model.voyage.Voyage;
import se.citerus.dddsample.domain.model.voyage.VoyageNumber;

public class RouteSpecificationTest {

  final Voyage hongKongTokyoNewYork = new Voyage.Builder(
    new VoyageNumber("V001"), LocationClient.sampleLocationsGetLocation("HONGKONG").getName()).
    addMovement(LocationClient.sampleLocationsGetLocation("TOKYO").getName(), toDate("2009-02-01"), toDate("2009-02-05")).
    addMovement(LocationClient.sampleLocationsGetLocation("NEWYORK").getName(), toDate("2009-02-06"), toDate("2009-02-10")).
    addMovement(LocationClient.sampleLocationsGetLocation("HONGKONG").getName(), toDate("2009-02-11"), toDate("2009-02-14")).
    build();

  final Voyage dallasNewYorkChicago = new Voyage.Builder(
    new VoyageNumber("V002"), LocationClient.sampleLocationsGetLocation("DALLAS").getName()).
    addMovement(LocationClient.sampleLocationsGetLocation("NEWYORK").getName(), toDate("2009-02-06"), toDate("2009-02-07")).
    addMovement(LocationClient.sampleLocationsGetLocation("CHICAGO").getName(), toDate("2009-02-12"), toDate("2009-02-20")).
    build();

  // TODO:
  // it shouldn't be possible to create Legs that have load/unload locations
  // and/or dates that don't match the voyage's carrier movements.
  final Itinerary itinerary = new Itinerary(Arrays.asList(
      new Leg(hongKongTokyoNewYork, LocationClient.sampleLocationsGetLocation("HONGKONG").getName(), LocationClient.sampleLocationsGetLocation("NEWYORK").getName(),
              toDate("2009-02-01"), toDate("2009-02-10")),
      new Leg(dallasNewYorkChicago, LocationClient.sampleLocationsGetLocation("NEWYORK").getName(), LocationClient.sampleLocationsGetLocation("CHICAGO").getName(),
              toDate("2009-02-12"), toDate("2009-02-20")))
  );
  @Test
  public void testIsSatisfiedBy_Success() {
    RouteSpecification routeSpecification = new RouteSpecification(
      LocationClient.sampleLocationsGetLocation("HONGKONG").getName(), LocationClient.sampleLocationsGetLocation("CHICAGO").getName(), toDate("2009-03-01")
    );

    assertThat(routeSpecification.isSatisfiedBy(itinerary)).isTrue();
  }

  @Test
  public void testIsSatisfiedBy_WrongOrigin() {
    RouteSpecification routeSpecification = new RouteSpecification(
      LocationClient.sampleLocationsGetLocation("HANGZOU").getName(), LocationClient.sampleLocationsGetLocation("CHICAGO").getName(), toDate("2009-03-01")
    );

    assertThat(routeSpecification.isSatisfiedBy(itinerary)).isFalse();
  }
  @Test
  public void testIsSatisfiedBy_WrongDestination() {
    RouteSpecification routeSpecification = new RouteSpecification(
      LocationClient.sampleLocationsGetLocation("HONGKONG").getName(), LocationClient.sampleLocationsGetLocation("DALLAS").getName(), toDate("2009-03-01")
    );

    assertThat(routeSpecification.isSatisfiedBy(itinerary)).isFalse();
  }
  @Test
  public void testIsSatisfiedBy_MissedDeadline() {
    RouteSpecification routeSpecification = new RouteSpecification(
      LocationClient.sampleLocationsGetLocation("HONGKONG").getName(), LocationClient.sampleLocationsGetLocation("CHICAGO").getName(), toDate("2009-02-15")
    );

    assertThat(routeSpecification.isSatisfiedBy(itinerary)).isFalse();
  }

}
