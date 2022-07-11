package se.citerus.dddsample.infrastructure.routing;

import com.pathfinder.api.GraphTraversalService;
import com.pathfinder.internal.GraphDAOStub;
import com.pathfinder.internal.GraphTraversalServiceImpl;
import org.junit.Before;
import org.junit.Test;
import se.citerus.dddsample.client.Location;
import se.citerus.dddsample.client.LocationClient;
import se.citerus.dddsample.domain.model.cargo.*;
import se.citerus.dddsample.domain.model.voyage.SampleVoyages;
import se.citerus.dddsample.domain.model.voyage.VoyageNumber;
import se.citerus.dddsample.domain.model.voyage.VoyageRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExternalRoutingServiceTest {

  private ExternalRoutingService externalRoutingService;
  private VoyageRepository voyageRepository;

  @Before
  public void setUp() {
    externalRoutingService = new ExternalRoutingService();

    voyageRepository = mock(VoyageRepository.class);
    externalRoutingService.setVoyageRepository(voyageRepository);

    GraphTraversalService graphTraversalService = new GraphTraversalServiceImpl(new GraphDAOStub() {
      public List<String> listLocations() {
        return Arrays.asList(LocationClient.sampleLocationsGetLocation("TOKYO").getUnLocode().getUnlocode(), LocationClient.sampleLocationsGetLocation("STOCKHOLM").getUnLocode().getUnlocode(), LocationClient.sampleLocationsGetLocation("GOTHENBURG").getUnLocode().getUnlocode());
      }

      public void storeCarrierMovementId(String cmId, String from, String to) {
      }
    });
    externalRoutingService.setGraphTraversalService(graphTraversalService);
  }

  // TODO this test belongs in com.pathfinder
  @Test
  public void testCalculatePossibleRoutes() {
    TrackingId trackingId = new TrackingId("ABC");
    RouteSpecification routeSpecification = new RouteSpecification(LocationClient.sampleLocationsGetLocation("HONGKONG").getName(), LocationClient.sampleLocationsGetLocation("HELSINKI").getName(), new Date());
    Cargo cargo = new Cargo(trackingId, routeSpecification);

    when(voyageRepository.find(isA(VoyageNumber.class))).thenReturn(SampleVoyages.CM002);

    List<Itinerary> candidates = externalRoutingService.fetchRoutesForSpecification(routeSpecification);
    assertThat(candidates).isNotNull();

    for (Itinerary itinerary : candidates) {
      List<Leg> legs = itinerary.legs();
      assertThat(legs).isNotNull();
      assertThat(legs.isEmpty()).isFalse();

      // Cargo origin and start of first leg should match
      assertThat(legs.get(0).loadLocation()).isEqualTo(cargo.origin());

      // Cargo final destination and last leg stop should match
      String lastLegStop = legs.get(legs.size() - 1).unloadLocation();
      assertThat(lastLegStop).isEqualTo(cargo.routeSpecification().destination());

      for (int i = 0; i < legs.size() - 1; i++) {
        // Assert that all legs are connected
        assertThat(legs.get(i + 1).loadLocation()).isEqualTo(legs.get(i).unloadLocation());
      }
    }
  }

}
