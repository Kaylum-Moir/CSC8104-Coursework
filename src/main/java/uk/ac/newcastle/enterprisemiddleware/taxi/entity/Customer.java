package uk.ac.newcastle.enterprisemiddleware.taxi.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

// Customer Entity
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    @Pattern(regexp = "^[A-Za-z ]+$", message = "The name must include only letters and spaces.")
    private String name;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Pattern(regexp = "^0\\d{10}$", message = "Phone number must be 11 digits and begin with a 0.")
    private String phone;


    //https://www.baeldung.com/jpa-cascade-types
    @OneToMany(
        mappedBy = "customer",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonIgnore
    private List<Booking> bookings = new ArrayList<>();

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
