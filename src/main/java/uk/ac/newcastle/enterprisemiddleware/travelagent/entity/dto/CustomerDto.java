package uk.ac.newcastle.enterprisemiddleware.travelagent.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerDto {

    public Long id;
    public String name;
    public String email;

    @JsonProperty("phoneNumber")
    public String phone;
}
