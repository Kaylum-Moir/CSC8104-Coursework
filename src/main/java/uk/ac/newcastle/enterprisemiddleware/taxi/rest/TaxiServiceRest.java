package uk.ac.newcastle.enterprisemiddleware.taxi.rest;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Customer;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Taxi;
import uk.ac.newcastle.enterprisemiddleware.taxi.service.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.taxi.service.TaxiService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

// https://quarkus.io/guides/hibernate-orm
// https://quarkus.io/guides/rest-json
@Path("/taxis")
@Tag(name = "Taxis", description = "Create and list taxis.")
public class TaxiServiceRest {
    @Inject
    TaxiService taxiService;

    @GET
    public Response listTaxis(){
        List<Taxi> taxis = taxiService.listTaxis();
        return Response.ok(taxis).build();
    }

    @POST
    @Transactional
    public Response createTaxi(@Valid Taxi taxi){
        taxiService.createTaxi(taxi);

        URI location = URI.create("/taxis/" + taxi.getId());
        return Response.created(location).entity(taxi).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTaxi(Long id) {
        taxiService.deleteTaxi(id);
        return Response.noContent().build();
    }
}
