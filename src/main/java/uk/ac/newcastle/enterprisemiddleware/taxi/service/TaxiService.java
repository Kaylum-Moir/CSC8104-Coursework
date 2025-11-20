package uk.ac.newcastle.enterprisemiddleware.taxi.service;

import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Customer;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Taxi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

// https://quarkus.io/guides/hibernate-orm
@ApplicationScoped
public class TaxiService {
    @Inject
    EntityManager em;

    @Transactional
    public void createTaxi(Taxi taxi) {
        em.persist(taxi);
    }

    // Returns a list of existing taxis in name order
    public List<Taxi> listTaxis() {
        return em.createQuery("SELECT c FROM Customer c ORDER BY c.name", Taxi.class).getResultList();
    }

    public Taxi findByID(Long id) {
        return em.find(Taxi.class, id);
    }
}
