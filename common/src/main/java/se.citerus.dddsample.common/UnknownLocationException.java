package se.citerus.dddsample.common;

import se.citerus.dddsample.client.UnLocode;

// TODO Circular dependency. Requires monolith to extend CannotCreateHandlingEventException
public class UnknownLocationException extends CannotCreateHandlingEventException {

  private final UnLocode unlocode;

  public UnknownLocationException(final UnLocode unlocode) {
    this.unlocode = unlocode;
  }

  @Override
  public String getMessage() {
    return "No location with UN locode " + unlocode.getUnlocode() + " exists in the system";
  }
}
