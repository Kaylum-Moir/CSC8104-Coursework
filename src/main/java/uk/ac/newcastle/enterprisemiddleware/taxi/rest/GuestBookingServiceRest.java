package uk.ac.newcastle.enterprisemiddleware.taxi.rest;

import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Booking;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.GuestBooking;
import uk.ac.newcastle.enterprisemiddleware.taxi.service.GuestBookingService;

import javax.inject.Inject;
import javax.transaction.UserTransaction;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/guestbookings")
public class GuestBookingServiceRest {
    @Inject
    UserTransaction userTransaction;

    @Inject
    GuestBookingService guestBookingService;

    @POST
    public Response createGuestBooking(GuestBooking guestBooking) {
        Booking booking;
        try {
            userTransaction.begin();
            booking = guestBookingService.createGuestBooking(guestBooking);
            userTransaction.commit();
        } catch (Exception e) {
            try {
                userTransaction.rollback(); // Try to rollback any changes if an error occurs
            } catch (Exception ignore) {

            }
            return Response.serverError().build();
        }

        URI loc = URI.create("/bookings/" + booking.getId());
        return Response.created(loc).entity(booking).build();
    }
}
