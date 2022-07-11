package se.citerus.dddsample.interfaces.booking.facade.internal.assembler;

import se.citerus.dddsample.client.LocationClient;
import se.citerus.dddsample.domain.model.cargo.Cargo;
import se.citerus.dddsample.domain.model.cargo.Leg;
import se.citerus.dddsample.domain.model.cargo.RoutingStatus;
import se.citerus.dddsample.interfaces.booking.facade.dto.CargoRoutingDTO;

/**
 * Assembler class for the CargoRoutingDTO.
 */
public class CargoRoutingDTOAssembler {

  /**
   *
   * @param cargo cargo
   * @return A cargo routing DTO
   */
  public CargoRoutingDTO toDTO(final Cargo cargo) {
    final CargoRoutingDTO dto = new CargoRoutingDTO(
      cargo.trackingId().idString(),
      LocationClient.sampleLocationsGetLocation(cargo.routeSpecification().origin().idString()).getUnLocode().getUnlocode(),
      LocationClient.sampleLocationsGetLocation(cargo.routeSpecification().destination().idString()).getUnLocode().getUnlocode(),
      cargo.routeSpecification().arrivalDeadline(),
      cargo.delivery().routingStatus().sameValueAs(RoutingStatus.MISROUTED));
    for (Leg leg : cargo.itinerary().legs()) {
      dto.addLeg(
        leg.voyage().voyageNumber().idString(),
        LocationClient.sampleLocationsGetLocation(leg.loadLocation().idString()).getUnLocode().getUnlocode(),
        LocationClient.sampleLocationsGetLocation(leg.unloadLocation().idString()).getUnLocode().getUnlocode(),
        leg.loadTime(),
        leg.unloadTime());
    }
    return dto;
  }

}
