package uk.ac.newcastle.enterprisemiddleware.travelagent.entity.dto;


import java.time.LocalDate;

public class HotelBooking {
    private Long id;
    public Hotel hotel;
    public CustomerDto customer;
    public LocalDate date;
}
