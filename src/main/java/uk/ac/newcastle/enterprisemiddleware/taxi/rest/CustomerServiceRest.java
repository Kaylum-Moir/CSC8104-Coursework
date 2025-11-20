package uk.ac.newcastle.enterprisemiddleware.taxi.rest;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Customer;
import uk.ac.newcastle.enterprisemiddleware.taxi.service.CustomerService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

// https://quarkus.io/guides/hibernate-orm
// https://quarkus.io/guides/rest-json
@Path("/customers")
@Tag(name = "Customers", description = "Create and list customers.")
public class CustomerServiceRest {
    @Inject
    CustomerService customerService;

    @GET
    public Response listCustomers(){
        List<Customer> customers = customerService.listCustomers();
        return Response.ok(customers).build();
    }

    @POST
    @Transactional
    public Response createCustomer(Customer customer){
        customerService.createCustomer(customer);

        URI location = URI.create("/customers/" + customer.getId());
        return Response.created(location).entity(customer).build();
    }
}
