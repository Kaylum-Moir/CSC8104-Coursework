package uk.ac.newcastle.enterprisemiddleware.travelagent.entity;

import io.smallrye.common.constraint.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

public class TABookingRequest {

    @NotNull
    @Schema(example = "1", required = true)
    private Long customerId;

    @NotNull
    @Schema(example = "1", required = true)
    private Long taxiId;

    @NotNull
    @Schema(example = "1", required = true)
    private Long hotelId;

    @NotNull
    @Schema(example = "1", required = true)
    private Long flightId;

    @NotNull
    private LocalDate date;

    @NotNull
    private Integer seats;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(Long taxiId) {
        this.taxiId = taxiId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
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
