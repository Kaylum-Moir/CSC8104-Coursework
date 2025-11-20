package uk.ac.newcastle.enterprisemiddleware.taxi.rest;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Booking;
import uk.ac.newcastle.enterprisemiddleware.taxi.service.BookingService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

// https://quarkus.io/guides/hibernate-orm
// https://quarkus.io/guides/rest-json
@Path("/bookings")
@Tag(name = "Bookings", description = "Create and list bookings.")
public class BookingServiceRest {
    @Inject
    BookingService bookingService;

    @GET
    public Response listBookings(@QueryParam("customer") Long customerId){
        List<Booking> bookings;
        if (customerId != null) {
            bookings = bookingService.findByCustomer(customerId);
        }
        else {
            bookings = bookingService.listBookings();
        }
        return Response.ok(bookings).build();
    }

    @POST
    @Transactional
    public Response createBooking(@Valid Booking booking){
        bookingService.createBooking(booking);

        URI location = URI.create("/bookings/" + booking.getId());
        return Response.created(location).entity(booking).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteBooking(@PathParam("id") Long id) {
        boolean confirmation = bookingService.deleteByID(id);
        if (!confirmation) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
