package se.citerus.dddsample.interfaces.booking.facade.internal.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import se.citerus.dddsample.client.Location;
import se.citerus.dddsample.client.LocationClient;
import se.citerus.dddsample.interfaces.booking.facade.dto.LocationDTO;

public class LocationDTOAssemblerTest {

  @Test
  public void testToDTOList() {
    final LocationDTOAssembler assembler = new LocationDTOAssembler();
    final List<Location> locationList = Arrays.asList(LocationClient.sampleLocationsGetLocation("STOCKHOLM"), LocationClient.sampleLocationsGetLocation("HAMBURG"));

    final List<LocationDTO> dtos = assembler.toDTOList(locationList);

    assertThat(dtos).hasSize(2);

    LocationDTO dto = dtos.get(0);
    assertThat(dto.getUnLocode()).isEqualTo("SESTO");
    assertThat(dto.getName()).isEqualTo("Stockholm");

    dto = dtos.get(1);
    assertThat(dto.getUnLocode()).isEqualTo("DEHAM");
    assertThat(dto.getName()).isEqualTo("Hamburg");
  }

}
