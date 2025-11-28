package uk.ac.newcastle.enterprisemiddleware.taxi.rest;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Booking;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Customer;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.GuestBooking;
import uk.ac.newcastle.enterprisemiddleware.taxi.service.GuestBookingService;

import javax.inject.Inject;
import javax.transaction.UserTransaction;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/guestbookings")
@Tag(name = "GuestBookings", description = "Create a Guest Booking.")
public class GuestBookingServiceRest {
    @Inject
    UserTransaction userTransaction;

    @Inject
    GuestBookingService guestBookingService;

    @POST
    @Operation(
            summary = "Create Guest Booking",
            description = "Creates a Guest Booking with a new Customer."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Guest Booking Created.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Booking.class)
                    )
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "An error occurred while creating a Guest Booking."
            )
    })
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
