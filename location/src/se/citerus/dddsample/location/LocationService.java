package se.citerus.dddsample.location;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class LocationService {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder
                .forPort(8080) // TODO Port should be different for each service ? Doesn't have to be if IP is different I suppose? Mostly network config. For now we assume the microservices are running as a distributed monolith on the same computer?
                .addService(new LocationServiceImpl()).build();

        server.start();
        System.out.println("Started Location Service on port 8080");
        server.awaitTermination();
    }
}
