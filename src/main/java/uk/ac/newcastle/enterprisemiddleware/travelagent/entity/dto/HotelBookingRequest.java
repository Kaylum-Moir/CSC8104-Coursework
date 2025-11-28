package uk.ac.newcastle.enterprisemiddleware.travelagent.entity.dto;

import java.time.LocalDate;

public class HotelBookingRequest {
    public Long customerId;
    public Long hotelId;
    public LocalDate date;
}
