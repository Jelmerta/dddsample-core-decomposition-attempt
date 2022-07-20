//package client.rmiserver;
//
//import client.rmiinterface.RMIInterface;
//package se.citerus.dddsample.domain.model.location.UnLocode;

//
//import java.rmi.RemoteException;
//import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;
//import java.rmi.server.UnicastRemoteObject;
//
//public class ServerOperation extends UnicastRemoteObject implements RMIInterface {
//
////    private String unlocode;
////
////    // Country code is exactly two letters.
////    // Location code is usually three letters, but may contain the numbers 2-9 as well
////    private static final Pattern VALID_PATTERN = Pattern.compile("[a-zA-Z]{2}[a-zA-Z2-9]{3}");
////
////    /**
////     * Constructor.
////     *
////     * @param countryAndLocation Location string.
////     */
////    public UnLocode(final String countryAndLocation) {
////        Validate.notNull(countryAndLocation, "Country and location may not be null");
////        Validate.isTrue(VALID_PATTERN.matcher(countryAndLocation).matches(),
////                countryAndLocation + " is not a valid UN/LOCODE (does not match pattern)");
////
////        this.unlocode = countryAndLocation.toUpperCase();
////    }
////
//////  @Override public Long getId() {
//////    return id;
//////  }
////
////    // TODO Is this a bad way to do it? Still direct dependency on this object instead of on client?
////    public String idString() {
//////        return unlocode;
////        return ClientOperation.idString(this);
////    }
////
////    public boolean equals(final Object o) {
////        if (this == o) return true;
////        if (o == null || getClass() != o.getClass()) return false;
////
////        UnLocode other = (UnLocode) o;
////
////        return sameValueAs(other);
////    }
////
////    @Override
////    public int hashCode() {
////        return unlocode.hashCode();
////    }
////
////    public boolean sameValueAs(UnLocode other) {
////        return other != null && this.unlocode.equals(other.unlocode);
////    }
////
////    @Override
////    public String toString() {
////        return idString();
////    }
////
////    UnLocode() {
////        // Needed by Hibernate
////    }
//    private static final long serialVersionUID = 1L;
//    protected ServerOperation() throws RemoteException {
//        super();
//    }
//
//    @Override
//    public UnLocode UnLocode(final String countryAndLocation) throws RemoteException {
//        return new UnLocode(countryAndLocation);
//    }
//
//    @Override
//    public String idString() throws RemoteException {
//        return unLocode.toString();
//        // TODO Here we actually just want to calld idString again. This is not the solution.
//    }
//
//    public static void main(String[] args){
//
//        try {
//            ServerOperation serverOperation = new ServerOperation();
//            Registry registry = LocateRegistry.createRegistry(1099);
//            registry.rebind("//localhost/MyServer", serverOperation);
//            System.err.println("Server ready");
//        } catch (Exception e) {
//            System.err.println("Server exception: " + e);
//            e.printStackTrace();
//        }
//    }
//
//}