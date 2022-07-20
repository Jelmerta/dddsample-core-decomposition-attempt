package client.rmiinterface;

import se.citerus.dddsample.client.location.UnLocode;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UnLocodeInterface extends Remote {
    String idString() throws RemoteException;

//    42
// https://stackoverflow.com/questions/1718112/tostring-equals-and-hashcode-in-an-interface
//All objects in Java inherit from java.lang.Object and Object provides default implementations of those methods.
//
//If your interface contains other methods, Java will complain if you don't implement the interface fully by providing an implementation of those methods. But in the case of equals(), hashCode() and toString() (as well as a few others that you didn't mention) the implementation already exists.
//
//One way you might be able to accomplish what you want is by providing a different method in the interface, say, toPrettyString() or something like that. Then you can call that method instead of the default toString() method.
//    boolean equals(final Object o) throws RemoteException;
//    int hashCode() throws RemoteException;
//    String toString() throws RemoteException;


    // TODO We should probably just detect if any of these methods are implemented in the base class and add them if necessary with a different method name

    // TODO Do we want these calls? EqualsCall is difficult because RMI has complains related to parameter being passed
    //final Object o
//    boolean equalsCall(final Object other) throws RemoteException;
//    int hashCodeCall() throws RemoteException;
//    String toStringCall() throws RemoteException;

    boolean sameValueAs(UnLocodeInterface other) throws RemoteException;

    String getUnlocode() throws RemoteException;
    void setUnlocode(String unlocode) throws RemoteException;

    // Only required (so far) if passed by value TODO Could we think of a better solution that does not expose this in interface? Casting interface to impl (((UnLocodeImpl) other).unLocode) is not possible.
    UnLocode getWrappedObject() throws RemoteException; // Does it need this exception at all? Only internally used
}
