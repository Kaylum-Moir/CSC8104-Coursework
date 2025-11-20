package uk.ac.newcastle.enterprisemiddleware.taxi.service;

import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Customer;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Taxi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

// https://quarkus.io/guides/hibernate-orm
@ApplicationScoped
public class TaxiService {
    @Inject
    EntityManager em;

    @Transactional
    public void createTaxi(Taxi taxi) {
        // Ensure the reg does not already exist for another user.
        long count = em.createQuery("SELECT COUNT(c) FROM Taxi c where c.reg = :reg", Long.class)
                .setParameter("reg", taxi.getReg())
                .getSingleResult();
        if (count > 0){
            throw new WebApplicationException("A taxi with that Registration Number already exists.", Response.Status.CONFLICT);
        }

        em.persist(taxi);
    }

    // Returns a list of existing taxis in name order
    public List<Taxi> listTaxis() {
        return em.createQuery("SELECT c FROM Taxi c ORDER BY c.reg", Taxi.class).getResultList();
    }

    public Taxi findByID(Long id) {
        return em.find(Taxi.class, id);
    }
}
