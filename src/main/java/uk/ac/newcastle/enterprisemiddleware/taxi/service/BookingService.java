package uk.ac.newcastle.enterprisemiddleware.taxi.service;

import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Booking;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

// https://quarkus.io/guides/hibernate-orm
@ApplicationScoped
public class BookingService {
    @Inject
    EntityManager em;

    @Transactional
    public void createBooking(Booking booking) {
        // Ensure the email address does not already exist for another user.
        long count = em.createQuery("SELECT COUNT(c) FROM Booking c WHERE c.taxi = :taxi AND c.date = :date", Long.class)
                                .setParameter("taxi", booking.getTaxi())
                                .setParameter("date", booking.getDate())
                                .getSingleResult();
        if (count > 0){
            throw new WebApplicationException("This taxi is unavailable for that date.", Response.Status.CONFLICT);
        }

        em.persist(booking);
    }

    // Returns a list of existing bookings in name order
    public List<Booking> listBookings() {
        return em.createQuery("SELECT c FROM Booking c", Booking.class).getResultList();
    }

    public Booking findByID(Long id) {
        return em.find(Booking.class, id);
    }

    public boolean deleteByID(Long id) {
        Booking target = em.find(Booking.class, id);
        if (target == null) {
            return false;
        }
        em.remove(target);
        return true;
    }

    public List<Booking> findByCustomer(Long id) {
        return em.createQuery("SELECT c FROM Booking c WHERE c.customer.id = :cid", Booking.class).setParameter("cid", id).getResultList();
    }
}
