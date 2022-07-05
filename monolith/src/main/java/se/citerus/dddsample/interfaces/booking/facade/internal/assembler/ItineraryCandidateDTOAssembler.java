package se.citerus.dddsample.interfaces.booking.facade.internal.assembler;

import se.citerus.dddsample.client.Location;
import se.citerus.dddsample.client.LocationClient;
import se.citerus.dddsample.client.UnLocode;
import se.citerus.dddsample.domain.model.cargo.Itinerary;
import se.citerus.dddsample.domain.model.cargo.Leg;
import se.citerus.dddsample.domain.model.location.LocationRepository;
import se.citerus.dddsample.domain.model.voyage.Voyage;
import se.citerus.dddsample.domain.model.voyage.VoyageNumber;
import se.citerus.dddsample.domain.model.voyage.VoyageRepository;
import se.citerus.dddsample.interfaces.booking.facade.dto.LegDTO;
import se.citerus.dddsample.interfaces.booking.facade.dto.RouteCandidateDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Assembler class for the ItineraryCandidateDTO.
 */
public class ItineraryCandidateDTOAssembler {

  /**
   * @param itinerary itinerary
   * @return A route candidate DTO
   */
  public RouteCandidateDTO toDTO(final Itinerary itinerary) {
    final List<LegDTO> legDTOs = new ArrayList<LegDTO>(itinerary.legs().size());
    for (Leg leg : itinerary.legs()) {
      legDTOs.add(toLegDTO(leg));
    }
    return new RouteCandidateDTO(legDTOs);
  }

  /**
   * @param leg leg
   * @return A leg DTO
   */
  protected LegDTO toLegDTO(final Leg leg) {
    final VoyageNumber voyageNumber = leg.voyage().voyageNumber();
    final UnLocode from = leg.loadLocation().getUnLocode();
    final UnLocode to = leg.unloadLocation().getUnLocode();
    return new LegDTO(voyageNumber.idString(), from.getUnlocode(), to.getUnlocode(), leg.loadTime(), leg.unloadTime());
  }

  /**
   * @param routeCandidateDTO route candidate DTO
   * @param voyageRepository voyage repository
   * @param locationRepository location repository
   * @return An itinerary
   */
  public Itinerary fromDTO(final RouteCandidateDTO routeCandidateDTO,
                           final VoyageRepository voyageRepository,
                           final LocationRepository locationRepository) {
    final List<Leg> legs = new ArrayList<Leg>(routeCandidateDTO.getLegs().size());
    for (LegDTO legDTO : routeCandidateDTO.getLegs()) {
      final VoyageNumber voyageNumber = new VoyageNumber(legDTO.getVoyageNumber());
      final Voyage voyage = voyageRepository.find(voyageNumber);
      final Location from = locationRepository.find(LocationClient.createUnLocode(legDTO.getFrom()));
      final Location to = locationRepository.find(LocationClient.createUnLocode(legDTO.getTo()));
      legs.add(new Leg(voyage, from, to, legDTO.getLoadTime(), legDTO.getUnloadTime()));
    }
    return new Itinerary(legs);
  }
}
