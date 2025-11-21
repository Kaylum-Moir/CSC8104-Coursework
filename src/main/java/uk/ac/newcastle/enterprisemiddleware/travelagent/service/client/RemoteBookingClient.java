package uk.ac.newcastle.enterprisemiddleware.travelagent.service;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

// --- HOTEL -----
@RegisterRestClient(configKey = "hotel-service")
@Path("/bookings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface HotelBookingClient {

    @POST
    RemoteBookingResponse createBooking(RemoteBookingRequest req);

    @DELETE
    @Path("/{id}")
    Response cancelBooking(@PathParam("id") Long id);
}


// --- FLIGHT -----
@RegisterRestClient(configKey = "flight-service")
@Path("/bookings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface FlightBookingClient {

    @POST
    RemoteBookingResponse createBooking(RemoteBookingRequest req);

    @DELETE
    @Path("/{id}")
    Response cancelBooking(@PathParam("id") Long id);
}

// — DTOs — //
class RemoteBookingRequest {
    public Long customerId;
    public Long commodityId; // hotelId or flightId
    public String date;
}

class RemoteBookingResponse {
    public Long id;
}