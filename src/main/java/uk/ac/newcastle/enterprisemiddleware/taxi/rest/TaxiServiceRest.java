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
@Tag(name = "Taxis", description = "Create, List and Delete Taxis.")
public class TaxiServiceRest {
    @Inject
    TaxiService taxiService;

    @GET
    @Operation(
            summary = "List Taxis",
            description = "Lists all taxis."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "taxis Listed.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Taxi.class)
                    )
            )
    })
    public Response listTaxis(){
        List<Taxi> taxis = taxiService.listTaxis();
        return Response.ok(taxis).build();
    }

    @POST
    @Transactional
    @Operation(
            summary = "Create Taxi",
            description = "Creates a new taxi with a unique email."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Taxi Created.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Taxi.class)
                    )
            ),
            @APIResponse(
                    responseCode = "409",
                    description = "Taxi with that reg already exists."
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid Taxi info."
            )
    })
    public Response createTaxi(@Valid Taxi taxi){
        taxiService.createTaxi(taxi);

        URI location = URI.create("/taxis/" + taxi.getId());
        return Response.created(location).entity(taxi).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(
            summary = "Delete Taxi",
            description = "Deletes a Taxi based on the Taxi ID"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Taxi Deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Taxi not Found."
            )
    })
    public Response deleteTaxi(Long id) {
        taxiService.deleteTaxi(id);
        return Response.noContent().build();
    }
}
