package se.citerus.dddsample.domain.model.location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import se.citerus.dddsample.client.LocationClient;
import se.citerus.dddsample.client.UnLocode;
import org.junit.Test;

public class UnLocodeTest {


  @Test
  public void testNew() {
    assertValid("AA234");
    assertValid("AAA9B");
    assertValid("AAAAA");
    
    assertInvalid("AAAA");
    assertInvalid("AAAAAA");
    assertInvalid("AAAA");
    assertInvalid("AAAAAA");
    assertInvalid("22AAA");
    assertInvalid("AA111");
    assertInvalid(null);
  }

  @Test
  public void testIdString() {
    assertThat(LocationClient.createUnLocode("AbcDe").getUnlocode()).isEqualTo("ABCDE");
  }

  @Test
  public void testEquals() {
    UnLocode allCaps = LocationClient.createUnLocode("ABCDE");
    UnLocode mixedCase = LocationClient.createUnLocode("aBcDe");

    assertThat(allCaps.equals(mixedCase)).isTrue();
    assertThat(mixedCase.equals(allCaps)).isTrue();
    assertThat(allCaps.equals(allCaps)).isTrue();

    assertThat(allCaps.equals(null)).isFalse();
    assertThat(allCaps.equals(LocationClient.createUnLocode("FGHIJ"))).isFalse();
  }

  @Test
  public void testHashCode() {
    UnLocode allCaps = LocationClient.createUnLocode("ABCDE");
    UnLocode mixedCase = LocationClient.createUnLocode("aBcDe");

    assertThat(mixedCase.hashCode()).isEqualTo(allCaps.hashCode());  
  }
  
  private void assertValid(String unlocode) {
    LocationClient.createUnLocode(unlocode);
  }

  private void assertInvalid(String unlocode) {
    try {
      LocationClient.createUnLocode(unlocode);
      fail("The combination [" + unlocode + "] is not a valid UnLocode");
    } catch (IllegalArgumentException expected) {}
  }

}
