package uk.ac.newcastle.enterprisemiddleware.travelagent.entity.dto;

import java.time.LocalDate;

public class Flight {
    public long  id;
    public String code;
    public String depart;
    public String destination;
    public LocalDate departureDate;
    public Integer seats;
}
