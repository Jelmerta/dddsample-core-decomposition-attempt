package client.rmiinterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LocationServiceInterface extends Remote {
    UnLocodeInterface newUnLocode(final String countryAndLocation) throws RemoteException;
//    UnLocode UnLocodeImpl(final String countryAndLocation) throws RemoteException;
//    String idString(String unLocodeId) throws RemoteException;
}
