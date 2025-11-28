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
@Tag(name = "Bookings", description = "Create, List and Delete Bookings.")
public class BookingServiceRest {
    @Inject
    BookingService bookingService;

    @GET
    @Operation(
            summary = "List Bookings",
            description = "Returns either all stored bookings or ones pertaining to a specific customer ID"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "List of Bookings.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Booking.class)
                    )
            )
    })
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
    @Operation(
            summary = "Create Booking",
            description = "Creates a new booking for a given customer and taxi for a specific date"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Booking Created.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Booking.class)
                    )
            ),
            @APIResponse(
                    responseCode = "409",
                    description = "Taxi is unavailable on that date."
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid Booking info."
            )
    })
    public Response createBooking(@Valid Booking booking){
        bookingService.createBooking(booking);

        URI location = URI.create("/bookings/" + booking.getId());
        return Response.created(location).entity(booking).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(
            summary = "Delete Booking",
            description = "Deletes a booking based on the booking ID"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Booking Deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Booking not Found."
            )
    })
    public Response deleteBooking(@PathParam("id") Long id) {
        boolean confirmation = bookingService.deleteByID(id);
        if (!confirmation) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
