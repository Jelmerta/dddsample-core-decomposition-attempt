package se.citerus.dddsample.infrastructure.persistence.hibernate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import se.citerus.dddsample.application.util.SampleDataGenerator;
import se.citerus.dddsample.client.Location;
import se.citerus.dddsample.client.LocationClient;
import se.citerus.dddsample.client.UnLocode;
import se.citerus.dddsample.domain.model.location.LocationRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration(value = {"/main/resources/context-infrastructure-persistence.xml"})
@Transactional
public class LocationRepositoryTest {
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        SampleDataGenerator.loadSampleData(jdbcTemplate, new TransactionTemplate(transactionManager));
    }

    @Test
    public void testFind() {
        final UnLocode melbourne = LocationClient.createUnLocode("AUMEL");
        Location location = locationRepository.find(melbourne);
        assertThat(location).isNotNull();
        assertThat(location.getUnLocode()).isEqualTo(melbourne);

        assertThat(locationRepository.find(LocationClient.createUnLocode("NOLOC"))).isNull();
    }

    @Test
    public void testFindAll() {
        List<Location> allLocations = locationRepository.findAll();

        assertThat(allLocations).isNotNull();
        assertThat(allLocations).hasSize(7);
    }

}
