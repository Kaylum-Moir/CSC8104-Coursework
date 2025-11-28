package uk.ac.newcastle.enterprisemiddleware.travelagent.entity.dto;


import java.time.LocalDate;

public class FlightBooking {
    private Long id;
    public Flight flight;
    public CustomerDto customer;
    public LocalDate date;
}
