package client.rmiserver;

import client.rmiinterface.ObjectInterface;
import se.citerus.dddsample.client.location.UnLocode;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ObjectImpl extends UnicastRemoteObject implements ObjectInterface {
    ObjectWrapper objectWrapper;
    public ObjectImpl(ObjectWrapper objectWrapper) throws RemoteException {
        this.objectWrapper = objectWrapper;
    }

    @Override
    public ObjectWrapper getWrappedObject() throws RemoteException {
        return objectWrapper;
    }
}
