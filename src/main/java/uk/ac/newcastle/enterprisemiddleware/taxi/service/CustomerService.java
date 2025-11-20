package uk.ac.newcastle.enterprisemiddleware.taxi.service;

import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

// https://quarkus.io/guides/hibernate-orm
@ApplicationScoped
public class CustomerService {
    @Inject
    EntityManager em;

    @Transactional
    public void createCustomer(Customer customer) {
        // Ensure the email address does not already exist for another user.
        em.createQuery("SELECT COUNT(c) FROM Customer c where c.email = :email", Long.class)
                                .setParameter("email", customer.getEmail())
                                .getSingleResult();

        em.persist(customer);
    }

    // Returns a list of existing customers in name order
    public List<Customer> listCustomers() {
        return em.createQuery("SELECT c FROM Customer c ORDER BY c.name", Customer.class).getResultList();
    }

    public Customer findByID(Long id) {
        return em.find(Customer.class, id);
    }
}
