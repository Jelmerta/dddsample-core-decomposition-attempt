package client.rmiserver;

import client.rmiinterface.LocationServiceInterface;
import client.rmiinterface.UnLocodeInterface;
import se.citerus.dddsample.client.location.UnLocode;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LocationService extends UnicastRemoteObject implements LocationServiceInterface {

    protected LocationService() throws RemoteException {
    }

    @Override
    public UnLocodeInterface newUnLocode(String countryAndLocation) throws RemoteException {
        UnLocode unLocode = new UnLocode(countryAndLocation);
        UnLocodeImpl unLocodeImpl = new UnLocodeImpl(unLocode);
        Registry registry = LocateRegistry.getRegistry(1099);
        registry.rebind("//localhost/MyServer/" + unLocode.getUnlocode(), unLocodeImpl);
        // Registry bind unlocodeimpl? is that even required? How is it accessed otherwise? TODO
        return unLocodeImpl;
    }

    public static void main(String[] args) {
        // TODO Ugly full path to policy
        // Only required for passing references by value

        System.setProperty("java.security.policy", "file:///home/jelmer/Documents/Software Engineering/Master Project/projects/dddsample-micro/client/src/main/resources/security.policy");
        System.setProperty("java.security.policyfile", "file:///home/jelmer/Documents/Software Engineering/Master Project/projects/dddsample-micro/client/src/main/resources/security.policy");
        if (System.getSecurityManager() == null) {
            RMISecurityManager securityManager = new RMISecurityManager();
            System.setSecurityManager(securityManager);
        }


        try {
            LocationService locationService = new LocationService();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("//localhost/MyServer", locationService);
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e);
            e.printStackTrace();
        }
    }

}