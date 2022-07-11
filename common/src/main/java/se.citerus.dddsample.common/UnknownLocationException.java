package se.citerus.dddsample.common;

// TODO Circular dependency. Requires monolith to extend CannotCreateHandlingEventException
public class UnknownLocationException extends CannotCreateHandlingEventException {

  private final String unlocode;

  public UnknownLocationException(final String unlocode) {
    this.unlocode = unlocode;
  }

  @Override
  public String getMessage() {
    return "No location with UN locode " + unlocode + " exists in the system";
  }
}
