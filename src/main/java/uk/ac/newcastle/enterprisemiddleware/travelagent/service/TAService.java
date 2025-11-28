package uk.ac.newcastle.enterprisemiddleware.travelagent.service;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Booking;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Customer;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Taxi;
import uk.ac.newcastle.enterprisemiddleware.taxi.service.BookingService;
import uk.ac.newcastle.enterprisemiddleware.taxi.service.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.taxi.service.TaxiService;
import uk.ac.newcastle.enterprisemiddleware.travelagent.entity.TABooking;
import uk.ac.newcastle.enterprisemiddleware.travelagent.entity.TABookingRequest;
import uk.ac.newcastle.enterprisemiddleware.travelagent.entity.dto.FlightBooking;
import uk.ac.newcastle.enterprisemiddleware.travelagent.entity.dto.FlightBookingRequest;
import uk.ac.newcastle.enterprisemiddleware.travelagent.entity.dto.HotelBooking;
import uk.ac.newcastle.enterprisemiddleware.travelagent.entity.dto.HotelBookingRequest;
import uk.ac.newcastle.enterprisemiddleware.travelagent.rest.FlightClientRest;
import uk.ac.newcastle.enterprisemiddleware.travelagent.rest.HotelClientRest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.awt.print.Book;
import java.util.List;



@ApplicationScoped
public class TAService {
    @Inject
    EntityManager em;

    @Inject
    CustomerService customerService;

    @Inject
    TaxiService taxiService;

    @Inject
    BookingService bookingService;

    @Inject
    @RestClient
    HotelClientRest hotelClientRest;

    @Inject
    @RestClient
    FlightClientRest flightClientRest;

    // Remote IDs for travelagent customer is 1
    private static final Long REMOTE_AGENT_ID = 1L;

    @Transactional
    public TABooking createBooking(TABookingRequest request) {

        // Get local taxi entities
        Customer customer = customerService.findByID(request.getCustomerId());
        if  (customer == null) {
            throw new WebApplicationException("Customer not found.", Response.Status.NOT_FOUND);
        }

        Taxi taxi =  taxiService.findByID(request.getTaxiId());
        if (taxi == null) {
            throw new WebApplicationException("Taxi not found.", Response.Status.NOT_FOUND);
        }

        // Create taxi booking
        Booking taxiBooking = new Booking();
        taxiBooking.setCustomer(customer);
        taxiBooking.setTaxi(taxi);
        taxiBooking.setDate(request.getDate());

        taxiBooking = bookingService.createBooking(taxiBooking);


        HotelBooking hotelBooking = null;
        FlightBooking flightBooking = null;

        try {
            HotelBookingRequest hotelRequest = new HotelBookingRequest();
            hotelRequest.setCustomerId(REMOTE_AGENT_ID);
            hotelRequest.setHotelId(request.getHotelId());
            hotelRequest.setDate(request.getDate());
            hotelBooking = hotelClientRest.createBooking(hotelRequest);

            FlightBookingRequest flightRequest = new FlightBookingRequest();
            flightRequest.setCustomerId(REMOTE_AGENT_ID);
            flightRequest.setFlightId(request.getFlightId());
            flightRequest.setDate(request.getDate());
            flightRequest.setSeats(request.getSeats());
            flightBooking = flightClientRest.createBooking(flightRequest);

        } catch (Exception e) {
            // Failed, undo anything that succeeded
            if (hotelBooking != null && hotelBooking.getId() != null) {
                try {
                    hotelClientRest.deleteBooking(hotelBooking.getId());
                }  catch (Exception ignore) {}
            }

            if (flightBooking != null && flightBooking.getId() != null) {
                try {
                    flightClientRest.deleteBooking(flightBooking.getId());
                }  catch (Exception ignore) {}
            }

            if (taxiBooking != null && taxiBooking.getId() != null) {
                bookingService.deleteByID(taxiBooking.getId());
            }

            throw new  WebApplicationException("Failed to create booking. All partial bookings have been deleted.", Response.Status.INTERNAL_SERVER_ERROR);
        }

        // All bookings succeeded
        TABooking taBooking = new TABooking();
        taBooking.setCustomer(customer);
        taBooking.setTaxiBooking(taxiBooking);
        taBooking.setHotelBookingId(hotelBooking.getId());
        taBooking.setFlightBookingId(flightBooking.getId());
        taBooking.setDate(request.getDate());
        taBooking.setSeats(request.getSeats());

        em.persist(taBooking);
        return taBooking;
    }

    public TABooking findBooking(Long id) {
        return em.find(TABooking.class, id);
    }

    public List<TABooking> findAllBookings() {
        return em.createQuery("SELECT b FROM TABooking b", TABooking.class).getResultList();
    }

    public List<TABooking> findByCustomer(Long customerId) {
        return em.createQuery("SELECT b FROM TABooking b WHERE b.customer.id=:cid", TABooking.class)
                .setParameter("cid", customerId).getResultList();
    }


    @Transactional
    public void cancelBooking(Long id) {
        TABooking booking = em.find(TABooking.class, id);
        if  (booking == null) {
            throw new WebApplicationException("Booking not found.", Response.Status.NOT_FOUND);
        }

        try {
            hotelClientRest.deleteBooking(booking.getHotelBookingId());
        }  catch (Exception ignore) {}

        try {
            flightClientRest.deleteBooking(booking.getFlightBookingId());
        }   catch (Exception ignore) {}

        Booking taxiBooking = booking.getTaxiBooking();
        if  (taxiBooking != null &&  taxiBooking.getId() != null) {
            bookingService.deleteByID(taxiBooking.getId());
        }

        em.remove(booking);
    }

}
