package se.citerus.dddsample.application;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.citerus.dddsample.application.impl.BookingServiceImpl;
import se.citerus.dddsample.client.LocationClient;
import se.citerus.dddsample.client.UnLocode;
import se.citerus.dddsample.domain.model.cargo.Cargo;
import se.citerus.dddsample.domain.model.cargo.CargoRepository;
import se.citerus.dddsample.domain.model.cargo.TrackingId;
import se.citerus.dddsample.domain.service.RoutingService;

public class BookingServiceTest {

  BookingServiceImpl bookingService;
  CargoRepository cargoRepository;
  RoutingService routingService;

  @Before
  public void setUp() {
    cargoRepository = mock(CargoRepository.class);
    routingService = mock(RoutingService.class);
    bookingService = new BookingServiceImpl(cargoRepository, routingService);
  }

  @Test
  public void testRegisterNew() {
    TrackingId expectedTrackingId = new TrackingId("TRK1");
    UnLocode fromUnlocode = LocationClient.createUnLocode("USCHI");
    UnLocode toUnlocode = LocationClient.createUnLocode("SESTO");

    when(cargoRepository.nextTrackingId()).thenReturn(expectedTrackingId);

    TrackingId trackingId = bookingService.bookNewCargo(fromUnlocode, toUnlocode, new Date());
    assertThat(trackingId).isEqualTo(expectedTrackingId);
  }

  @After
  public void tearDown() {
    verify(cargoRepository, times(1)).store(isA(Cargo.class));
  }
}
