package client.rmiinterface;

import client.rmiserver.ObjectWrapper;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObjectInterface extends Remote {
    ObjectWrapper getWrappedObject() throws RemoteException;
}
