package uk.ac.newcastle.enterprisemiddleware.travelagent.entity;

/*
Created TravelAgent entity on three services as:
{
  "name": "TAgent",
  "email": "agent@Travel.com",
  "phoneNumber": "07123456789"
}

https://csc-8104-khwaja-basheer-git-khwaja786-dev.apps.rm3.7wse.p1.openshiftapps.com/q/swagger-ui/#/Customer%20Rest%20Service/post_customers
https://csc-8104-mingyuan-shao-shao-middleware-dev.apps.rm2.thpm.p1.openshiftapps.com/q/swagger-ui/#/Customer%20Rest%20Service/post_customers
*/

import io.smallrye.common.constraint.NotNull;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Booking;
import uk.ac.newcastle.enterprisemiddleware.taxi.entity.Customer;

import javax.persistence.*;
import java.time.LocalDate;
import javax.persistence.Entity;

@Entity
public class TABooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    @NotNull
    private Customer customer;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "taxi_booking_id")
    @NotNull
    private Booking taxiBooking;

    @Column(name = "hotel_booking_id", nullable = false)
    @NotNull
    private Long hotelBookingId;

    @Column(name = "flight_booking_id", nullable = false)
    @NotNull
    private Long flightBookingId;

    @Column(name = "travel_date", nullable = false)
    @NotNull
    private LocalDate date;

    @Column(name = "flight_seats",  nullable = false)
    @NotNull
    private Integer seats;


    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Booking getTaxiBooking() {
        return taxiBooking;
    }

    public void setTaxiBooking(Booking taxiBooking) {
        this.taxiBooking = taxiBooking;
    }

    public Long getHotelBookingId() {
        return hotelBookingId;
    }

    public void setHotelBookingId(Long hotelBookingId) {
        this.hotelBookingId = hotelBookingId;
    }

    public Long getFlightBookingId() {
        return flightBookingId;
    }

    public void setFlightBookingId(Long flightBookingId) {
        this.flightBookingId = flightBookingId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }
}
