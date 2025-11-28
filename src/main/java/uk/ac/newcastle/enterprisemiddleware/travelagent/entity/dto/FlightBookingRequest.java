package uk.ac.newcastle.enterprisemiddleware.travelagent.entity.dto;

import java.time.LocalDate;

public class FlightBookingRequest {
    public Long customerId;
    public Long flightId;
    public LocalDate date;
    public Integer seats;
}
