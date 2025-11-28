package uk.ac.newcastle.enterprisemiddleware.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Schema(name = "Taxi", description = "A Taxi Object storing ID, Registration and Seats.")
public class Taxi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    @Pattern(regexp = "^[A-Za-z0-9]{7}$") // Alphanum and 7 characters long
    private String reg;

    @Min(2)
    @Max(20)
    private int seats;


    //https://www.baeldung.com/jpa-cascade-types
    @OneToMany(
            mappedBy = "taxi",
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

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}
