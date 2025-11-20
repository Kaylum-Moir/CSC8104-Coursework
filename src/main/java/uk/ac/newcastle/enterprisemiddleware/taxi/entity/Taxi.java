package uk.ac.newcastle.enterprisemiddleware.taxi.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
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
