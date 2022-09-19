package se.citerus.dddsample.domain;

import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LocationService extends UnicastRemoteObject implements Remote {

    protected LocationService() throws RemoteException {
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
            UnLocodeService unLocodeService = new UnLocodeService();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("//localhost/Location", unLocodeService);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e);
            e.printStackTrace();
        }
    }

}