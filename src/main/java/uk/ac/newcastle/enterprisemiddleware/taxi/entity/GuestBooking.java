package uk.ac.newcastle.enterprisemiddleware.taxi.entity;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "GuestBooking", description = "Object that stores a new Customer and their Booking.")
public class GuestBooking {
    private Customer customer;
    private Booking booking;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
