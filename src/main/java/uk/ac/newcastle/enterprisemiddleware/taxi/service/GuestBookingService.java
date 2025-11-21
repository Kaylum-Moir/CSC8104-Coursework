package uk.ac.newcastle.enterprisemiddleware.taxi.service;

import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Booking;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Customer;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.GuestBooking;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.awt.print.Book;

@ApplicationScoped
public class GuestBookingService {

    @Inject
    CustomerService customerService;

    @Inject
    BookingService bookingService;

    public Booking createGuestBooking(GuestBooking guestBooking) {
        Customer customer = guestBooking.getCustomer();
        Booking booking = guestBooking.getBooking();

        customerService.createCustomer(customer);
        booking.setCustomer(customer);
        return bookingService.createBooking(booking);
    }
}
