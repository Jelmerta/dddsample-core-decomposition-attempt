package main.java.com.pathfinder.config;

import main.java.com.pathfinder.api.GraphTraversalService;
import main.java.com.pathfinder.internal.GraphDAO;
import main.java.com.pathfinder.internal.GraphDAOStub;
import main.java.com.pathfinder.internal.GraphTraversalServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathfinderApplicationContext {

    private GraphDAO graphDAO() {
        return new GraphDAOStub();
    }

    @Bean
    public GraphTraversalService graphTraversalService() {
        return new GraphTraversalServiceImpl(graphDAO());
    }
}