package se.citerus.dddsample.interfaces.booking.facade.internal.assembler;

import static org.assertj.core.api.Assertions.assertThat;
import static se.citerus.dddsample.domain.model.voyage.SampleVoyages.CM001;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import se.citerus.dddsample.client.Location;
import se.citerus.dddsample.client.LocationClient;
import se.citerus.dddsample.domain.model.cargo.Itinerary;
import se.citerus.dddsample.domain.model.cargo.Leg;
import se.citerus.dddsample.domain.model.voyage.VoyageRepository;
import se.citerus.dddsample.infrastructure.persistence.inmemory.VoyageRepositoryInMem;
import se.citerus.dddsample.interfaces.booking.facade.dto.LegDTO;
import se.citerus.dddsample.interfaces.booking.facade.dto.RouteCandidateDTO;

public class ItineraryCandidateDTOAssemblerTest {

  @Test
  public void testToDTO() {
    final ItineraryCandidateDTOAssembler assembler = new ItineraryCandidateDTOAssembler();

    final Location origin = LocationClient.sampleLocationsGetLocation("STOCKHOLM");
    final Location destination = LocationClient.sampleLocationsGetLocation("MELBOURNE");

    final Itinerary itinerary = new Itinerary(
      Arrays.asList(
        new Leg(CM001, origin.getName(), LocationClient.sampleLocationsGetLocation("SHANGHAI").getName(), new Date(), new Date()),
        new Leg(CM001, LocationClient.sampleLocationsGetLocation("ROTTERDAM").getName(), destination.getName(), new Date(), new Date())
      )
    );

    final RouteCandidateDTO dto = assembler.toDTO(itinerary);

    assertThat(dto.getLegs()).hasSize(2);
    LegDTO legDTO = dto.getLegs().get(0);
    assertThat(legDTO.getVoyageNumber()).isEqualTo("CM001");
    assertThat(legDTO.getFrom()).isEqualTo("SESTO");
    assertThat(legDTO.getTo()).isEqualTo("CNSHA");

    legDTO = dto.getLegs().get(1);
    assertThat(legDTO.getVoyageNumber()).isEqualTo("CM001");
    assertThat(legDTO.getFrom()).isEqualTo("NLRTM");
    assertThat(legDTO.getTo()).isEqualTo("AUMEL");
  }

  @Test
  public void testFromDTO() {
    final ItineraryCandidateDTOAssembler assembler = new ItineraryCandidateDTOAssembler();

    final List<LegDTO> legs = new ArrayList<LegDTO>();
    legs.add(new LegDTO("CM001", "AAAAA", "BBBBB", new Date(), new Date()));
    legs.add(new LegDTO("CM001", "BBBBB", "CCCCC", new Date(), new Date()));

    final VoyageRepository voyageRepository = new VoyageRepositoryInMem();


    // Tested call
    final Itinerary itinerary = assembler.fromDTO(new RouteCandidateDTO(legs), voyageRepository);

    
    assertThat(itinerary).isNotNull();
    assertThat(itinerary.legs()).isNotNull();
    assertThat(itinerary.legs()).hasSize(2);

    final Leg leg1 = itinerary.legs().get(0);
    assertThat(leg1).isNotNull();
    assertThat(leg1.loadLocation()).isEqualTo(LocationClient.sampleLocationsGetLocation("HONGKONG"));
    assertThat(leg1.unloadLocation()).isEqualTo(LocationClient.sampleLocationsGetLocation("TOKYO"));

    final Leg leg2 = itinerary.legs().get(1);
    assertThat(leg2).isNotNull();
    assertThat(leg2.loadLocation()).isEqualTo(LocationClient.sampleLocationsGetLocation("TOKYO"));
    assertThat(leg2.unloadLocation()).isEqualTo(LocationClient.sampleLocationsGetLocation("CHICAGO"));
  }
}
