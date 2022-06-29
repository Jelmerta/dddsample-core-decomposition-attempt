package main.java.domain;

import se.citerus.dddsample.domain.model.handling.CannotCreateHandlingEventException;

// TODO Circular dependency. Requires monolith to extend CannotCreateHandlingEventException
public class UnknownLocationException extends CannotCreateHandlingEventException {

  private final UnLocode unlocode;

  public UnknownLocationException(final UnLocode unlocode) {
    this.unlocode = unlocode;
  }

  @Override
  public String getMessage() {
    return "No location with UN locode " + unlocode.idString() + " exists in the system";
  }
}
