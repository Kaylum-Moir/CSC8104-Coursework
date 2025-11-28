package uk.ac.newcastle.enterprisemiddleware.travelagent.rest;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import uk.ac.newcastle.enterprisemiddleware.travelagent.entity.TABooking;
import uk.ac.newcastle.enterprisemiddleware.travelagent.entity.TABookingRequest;
import uk.ac.newcastle.enterprisemiddleware.travelagent.service.TAService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/travelagent/bookings")
@Tag(name = "TravelAgent", description = "Creates, lists and cancels bookings for the taxi service and a remote hotel and flights service.")
public class TAServiceRest {
    @Inject
    TAService taService;

    @POST
    @Transactional
    @Operation(
            summary = "Create a travelagent booking",
            description = "Creates a booking i nthe taxi, hotel and flight services"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Booking Created",
                    content = @Content(schema = @Schema(implementation = TABooking.class))
            ),
            @APIResponse(responseCode = "404", description = "Customer or Taxi not found."),
            @APIResponse(responseCode = "500", description = "System failed, remote bookings deleted.")
    })
    public Response createTravelAgentBooking(TABookingRequest request) {
        TABooking booking = taService.createBooking(request);
        URI uri = URI.create("/travelagent/bookings/" + booking.getId());
        return Response.created(uri).entity(booking).build();
    }


    @GET
    @Operation(
            summary = "List Travelagent bookings",
            description = "Return a list of all travelagent bookings or filter by customer ID"
    )
    @APIResponse(
            responseCode = "200",
            description = "List of bookings.",
            content = @Content(schema = @Schema(implementation = TABooking.class))
    )
    public Response listTravelAgentBookings(Long customerId) {
        if (customerId != null) {
            return Response.ok(taService.findBooking(customerId)).build();
        } else {
            return Response.ok(taService.findAllBookings()).build();
        }
    }

    @GET
    @Path("/{id}")
    @Operation(
            summary = "Return a travelagent booking.",
            description = "CReturns a travelagent booking from the booking ID."
    )
    @APIResponses({
            @APIResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TABooking.class))),
            @APIResponse(responseCode = "404", description = "Booking not found.")
    })
    public Response getTravelAgentBooking(Long id) {
        TABooking booking = taService.findBooking(id);
        if (booking == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(booking).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(
            summary = "Cancel a travelagent booking.",
            description = "Cancels the remote bookings of Taxi, Hotel and Flight"
    )
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Booking Cancelled"),
            @APIResponse(responseCode = "404", description = "Booking not found.")
    })
    public Response cancelTravelAgentBooking(Long id) {
        taService.cancelBooking(id);
        return Response.noContent().build();
    }
}
