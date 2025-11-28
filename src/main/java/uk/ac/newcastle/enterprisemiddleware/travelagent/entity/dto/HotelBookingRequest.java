package uk.ac.newcastle.enterprisemiddleware.travelagent.entity.dto;

import java.time.LocalDate;

public class HotelBookingRequest {
    public Long customerId;
    public Long hotelId;
    public LocalDate date;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
