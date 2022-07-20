package se.citerus.dddsample.domain.model.location;

import client.rmiinterface.LocationServiceInterface;
import client.rmiinterface.ObjectInterface;
import client.rmiinterface.UnLocodeInterface;
import client.rmiserver.ObjectImpl;
import client.rmiserver.ObjectWrapper;
import org.apache.commons.lang.Validate;
import se.citerus.dddsample.domain.shared.ValueObject;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * United nations location code.
 * <p/>
 * http://www.unece.org/cefact/locode/
 * http://www.unece.org/cefact/locode/DocColumnDescription.htm#LOCODE
 */
// TODO: java.lang.RuntimeException: java.rmi.ServerException: RemoteException occurred in server thread; nested exception is:
//	java.rmi.UnmarshalException: error unmarshalling arguments; nested exception is:
//	java.lang.ClassNotFoundException: se.citerus.dddsample.domain.model.location.UnLocode (no security manager: RMI class loader disabled)
//  Description: https://stackoverflow.com/questions/6322107/java-no-security-manager-rmi-class-loader-disabled
// Remote class loading can be tricky.
//
//The original post doesn't include any information about the code base. It may be that the client's security configuration is correct, but it has no access to the remote code. The classes are loaded directly from the "code base" by the client. They are not presented to the client by the service over the RMI connection. The service merely references an external source for the classes.
//
//The server should specify the system property java.rmi.server.codebase. The value must be a URL that is accessible to the client, from which the necessary classes can be loaded. If this is a file: URL, the file system must be accessible to the client.
public final class UnLocode implements ValueObject<UnLocode> {

  private static LocationServiceInterface service;
  private UnLocodeInterface unLocode; // This field can be null even though it is initialized (not null) in the constructor
  // Believe this happens because it is loaded from database and Hibernate does not store this field
  // We could dynamically get this from the location service lookup with some unique id?... Big trade-off though... Needed when deserialization is required, it seems?
  // Maybe null check?
  private String unlocode; // TODO Required for Hibernate. The getters and setters however, should also be implemented
  // TODO Finding this out probably requires runtime analysis or something...

  public String getUnlocode() {
    checkBinding();

    try {
      return unLocode.getUnlocode();
    } catch (RemoteException e) {
      throw new RuntimeException(e);
    }
  }

  public void setUnlocode(String unlocode) {
    checkBinding();

    this.unlocode = unlocode; // Note: This is not a good idea to duplicate the data here and in the location service, but Hibernate directly accesses the field for storage instead of using the getter, it seems.
    try {
      unLocode.setUnlocode(unlocode);
    } catch (RemoteException e) {
      throw new RuntimeException(e);
    }
  }

  // TODO service class/external facing. Therefore service wrapper
  //  package client.rmiclient;
	static {
		try {
            Registry registry = LocateRegistry.getRegistry();
            service = (LocationServiceInterface) registry.lookup("//localhost/MyServer");
		} catch (NotBoundException | RemoteException e) {
			throw new RuntimeException(e);
		}
	}
//
////	public static UnLocode createUnLocode(final String countryAndLocation) {
////		try {
////			return new UnLocodeImpl(countryAndLocation);//look_up.UnLocode(countryAndLocation);
////		} catch (RemoteException e) {
////			throw new RuntimeException(e); // TODO Error handling -> Very important to discuss
////		}
////	}
//
//	public String idString() {
//		try {
//			return look_up.idString();
//		} catch (RemoteException e) {
//			throw new RuntimeException(e); // TODO Error handling -> Very important to discuss
//		}
//	}
//}



//  private String unlocode; REQUIRED FOR HIBERNATE?

  // Country code is exactly two letters.
  // Location code is usually three letters, but may contain the numbers 2-9 as well
  private static final Pattern VALID_PATTERN = Pattern.compile("[a-zA-Z]{2}[a-zA-Z2-9]{3}");

  /**
   * Constructor.
   *
   * @param countryAndLocation Location string.
   */
  public UnLocode(final String countryAndLocation) {
    // Note: This is not a good idea to duplicate the data here and in the location service, but Hibernate directly accesses the field for storage instead of using the getter, it seems.
    Validate.notNull(countryAndLocation, "Country and location may not be null");
    Validate.isTrue(VALID_PATTERN.matcher(countryAndLocation).matches(),
            countryAndLocation + " is not a valid UN/LOCODE (does not match pattern)");
    this.unlocode = countryAndLocation.toUpperCase();

    try {
      this.unLocode = service.newUnLocode(countryAndLocation);
    } catch (RemoteException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * @return country code and location code concatenated, always upper case.
   */
  public String idString() {
    checkBinding();

    try {
      return unLocode.idString();
    } catch (RemoteException e) {
      throw new RuntimeException(e);
    }
  }

  private void checkBinding() {
    try {
      if (unLocode == null) {
        Registry registry = LocateRegistry.getRegistry();
        unLocode = (UnLocodeInterface) registry.lookup("//localhost/MyServer/" + unlocode); // TODO Could be an id instead?? Should be repoducable though. Maybe just concat all the field variables or something like that (security...), all constructor parameters?
      }
    } catch (NotBoundException | RemoteException e) {
      throw new RuntimeException(e);
    }
  }

//  public boolean equals(final Object o) {
//    checkBinding();
//    try {
//      ObjectInterface objectInterface = new ObjectImpl(o);
//      return unLocode.equalsCall(objectInterface);
//    } catch (RemoteException e) {
//      throw new RuntimeException(e);
//    }

//    ObjectWrapper objectWrapper = new ObjectWrapper(o);
//    try {
//      ObjectImpl objectInterface = new ObjectImpl(objectWrapper); // Use interface or not? TODO
//      Registry registry = LocateRegistry.getRegistry();
//      registry.rebind("//localhost/MyServer/Object/" + objectWrapper.hashCode(), objectInterface);

//    try {
//      return unLocode.equalsCall(o);
//    } catch (RemoteException e) {
//      throw new RuntimeException(e);
//    }
//  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UnLocode other = (UnLocode) o;

    return sameValueAs(other);
  }

  public int hashCode() {
    return getUnlocode().hashCode(); // TODO This is probably not the same value though... Can we get away with that? Service call is possible but not sure...
//    checkBinding();
//
//    try {
//      return unLocode.hashCodeCall();
//    } catch (RemoteException e) {
//      throw new RuntimeException(e);
//    }
  }

  public boolean sameValueAs(UnLocode other) {
    checkBinding();
    other.checkBinding();

    try {
      return unLocode.sameValueAs(other.unLocode);
    } catch (RemoteException e) {
      throw new RuntimeException(e);
    }
//    return other != null && this.service.equals(other.service);
  }

  public String toString() {
    return idString(); // TODO Different again... Can we get away with that? Do we want service call?
//    checkBinding();
//
//    try {
//      return unLocode.toStringCall();
//    } catch (RemoteException e) {
//      throw new RuntimeException(e);
//    }
  }

  UnLocode() {
    // Needed by Hibernate
  }

}
