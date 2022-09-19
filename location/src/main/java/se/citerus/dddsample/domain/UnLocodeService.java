package se.citerus.dddsample.domain;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class UnLocodeService
        extends UnicastRemoteObject implements client.rmiinterface.UnLocodeService {
    protected UnLocodeService() throws RemoteException {
    }

    @Override
    public String newUnLocode(String countryAndLocation) throws RemoteException {
        // TODO We are now preventing duplicates for dynamic loading of initial data. (Otherwise we will get duplicate data of unlocodes, but with different ids, one generated and one during startup) This is logic that should probably be human-generated, not perfect...
        // Checking for existence...
        Set<Map.Entry<String, UnLocode>> entries = UnLocodeStorageManager.unLocodeHashMap.entrySet();
        Optional<Map.Entry<String, UnLocode>> unLocodeEntry = entries.stream().filter(e -> e.getValue().getUnlocode().equalsIgnoreCase(countryAndLocation)).findFirst();
        if (unLocodeEntry.isPresent()) {
            return unLocodeEntry.get().getKey();
        }

        // Normal logic
        UnLocode unLocode = new UnLocode(countryAndLocation);
        return UnLocodeStorageManager.addUnLocode(unLocode);
    }

    @Override
    public String idString(String referenceId) throws RemoteException {
        System.out.println("reference id");
        System.out.println(referenceId);
        UnLocode unLocode = UnLocodeStorageManager.getUnLocode(referenceId);
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
    public boolean sameValueAs(String referenceId, String referenceId2) throws RemoteException {
        UnLocode unLocode = UnLocodeStorageManager.getUnLocode(referenceId);
        UnLocode other = UnLocodeStorageManager.getUnLocode(referenceId2);
        return unLocode.sameValueAs(other);
    }

    @Override
    public String getUnlocode(String referenceId) throws RemoteException {
        System.out.println("Retrieving UnLocode");
        System.out.println(referenceId);
        UnLocode unLocode = UnLocodeStorageManager.getUnLocode(referenceId);
        return unLocode.getUnlocode();
    }

    @Override
    public void setUnlocode(String referenceId, String unlocode) throws RemoteException {
        UnLocode unLocode = UnLocodeStorageManager.getUnLocode(referenceId);
        unLocode.setUnlocode(unlocode);
    }
}