package uk.ac.newcastle.enterprisemiddleware.taxi.rest;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Booking;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Customer;
import uk.ac.newcastle.enterprisemiddleware.taxi.service.CustomerService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
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
@Path("/customers")
@Tag(name = "Customers", description = "Create, List and Delete Customers.")
public class CustomerServiceRest {
    @Inject
    CustomerService customerService;

    @GET
    @Operation(
            summary = "List Customers",
            description = "Lists all customers."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Customers Listed.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class)
                    )
            )
    })
    public Response listCustomers(){
        List<Customer> customers = customerService.listCustomers();
        return Response.ok(customers).build();
    }

    @POST
    @Transactional
    @Operation(
            summary = "Create Customer",
            description = "Creates a new customer with a unique email."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Customer Created.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class)
                    )
            ),
            @APIResponse(
                    responseCode = "409",
                    description = "Customer with that email already exists."
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid Customer info."
            )
    })
    public Response createCustomer(@Valid Customer customer){
        customerService.createCustomer(customer);

        URI location = URI.create("/customers/" + customer.getId());
        return Response.created(location).entity(customer).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(
            summary = "Delete Customer",
            description = "Deletes a Customer based on the Customer ID"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Customer Deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Customer not Found."
            )
    })
    public Response deleteCustomer(Long id) {
        customerService.deleteCustomer(id);
        return Response.noContent().build();
    }
}
