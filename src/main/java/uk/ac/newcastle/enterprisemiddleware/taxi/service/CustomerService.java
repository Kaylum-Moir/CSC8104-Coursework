package uk.ac.newcastle.enterprisemiddleware.taxi.service;

import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

// https://quarkus.io/guides/hibernate-orm
@ApplicationScoped
public class CustomerService {
    @Inject
    EntityManager em;

    @Transactional
    public void createCustomer(Customer customer) {
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
