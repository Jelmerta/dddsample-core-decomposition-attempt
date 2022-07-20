package client.rmiserver;

import client.rmiinterface.UnLocodeInterface;
import se.citerus.dddsample.client.location.UnLocode;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UnLocodeImpl extends UnicastRemoteObject implements UnLocodeInterface {
    UnLocode unLocode;

    protected UnLocodeImpl(UnLocode unLocode) throws RemoteException {
        this.unLocode = unLocode;
    }

//    public static void main(String[] args){
//
//        try {
//            Registry registry = LocateRegistry.createRegistry(1099);
//            registry.rebind("//localhost/MyServer", serverOperation);
//            System.err.println("Server ready");
//        } catch (Exception e) {
//            System.err.println("Server exception: " + e);
//            e.printStackTrace();
//        }
//    }

    @Override
    public String idString() throws RemoteException {
        return unLocode.idString();
    }

    // TODO These are not possible because class extends RemoteObject?
    // Clashes with method from RemoteObject
//    @Override
//    public boolean equalsCall(final Object o) throws RemoteException {
//        return true;
//        return unLocode.equals(o);
//    }

//    @Override
//    public int hashCodeCall() throws RemoteException {
//        return unLocode.hashCode();
//    }

//    @Override
//    public String toStringCall() throws RemoteException {
//        return unLocode.toString();
//    }

    @Override
    public boolean sameValueAs(UnLocodeInterface other) throws RemoteException {
        return unLocode.sameValueAs(other.getWrappedObject());
    }

    @Override
    public String getUnlocode() throws RemoteException {
        return unLocode.getUnlocode();
    }

    @Override
    public void setUnlocode(String unlocode) throws RemoteException {
        unLocode.setUnlocode(unlocode);
    }

    @Override
    public UnLocode getWrappedObject() throws RemoteException {
        return unLocode;
    }
}