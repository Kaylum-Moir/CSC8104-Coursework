package uk.ac.newcastle.enterprisemiddleware.travelagent.rest;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import uk.ac.newcastle.enterprisemiddleware.travelagent.entity.dto.HotelBooking;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/bookings")
@RegisterRestClient(configKey = "hotel-api")
public interface HotelClientRest {

    @POST
    HotelBooking createBooking(HotelBooking request);
}
