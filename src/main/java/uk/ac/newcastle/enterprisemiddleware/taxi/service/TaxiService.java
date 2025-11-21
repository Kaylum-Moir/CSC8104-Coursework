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
        em.createQuery("SELECT COUNT(c) FROM Taxi c where c.reg = :reg", Long.class)
                .setParameter("reg", taxi.getReg())
                .getSingleResult();

        em.persist(taxi);
    }

    // Returns a list of existing taxis in name order
    public List<Taxi> listTaxis() {
        return em.createQuery("SELECT c FROM Taxi c ORDER BY c.reg", Taxi.class).getResultList();
    }

    public Taxi findByID(Long id) {
        return em.find(Taxi.class, id);
    }


    @Transactional
    public void deleteTaxi(Long id) {
        Taxi taxi = em.find(Taxi.class, id);
        if (taxi == null) {
            throw new WebApplicationException(
                    "Taxi not found",
                    Response.Status.NOT_FOUND
            );
        }
        em.remove(taxi);
    }
}
