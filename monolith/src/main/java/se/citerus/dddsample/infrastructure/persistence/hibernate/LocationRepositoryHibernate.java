package se.citerus.dddsample.infrastructure.persistence.hibernate;

import org.springframework.stereotype.Repository;
import se.citerus.dddsample.client.Location;
import se.citerus.dddsample.client.UnLocode;
import se.citerus.dddsample.domain.model.location.LocationRepository;

import java.util.List;

// TODO This is not found, but pretty much required in the Location service...
@Repository
public class LocationRepositoryHibernate extends HibernateRepository implements LocationRepository {

  public Location find(final UnLocode unLocode) {
    return (Location) getSession().
      createQuery("from Location where unLocode = ?").
      setParameter(0, unLocode).
      uniqueResult();
  }

  public List<Location> findAll() {
    return getSession().createQuery("from Location").list();
  }

}
