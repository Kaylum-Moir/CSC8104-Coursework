package uk.ac.newcastle.enterprisemiddleware.travelagent.rest;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import uk.ac.newcastle.enterprisemiddleware.travelagent.entity.dto.FlightBooking;
import uk.ac.newcastle.enterprisemiddleware.travelagent.entity.dto.FlightBookingRequest;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/bookings")
@RegisterRestClient(configKey = "flight-api")
public interface FlightClientRest {

    @POST
    FlightBooking createBooking(FlightBookingRequest request);

    @DELETE
    @Path("/{id}")
    void deleteBooking(@PathParam("id") Long id);
}
