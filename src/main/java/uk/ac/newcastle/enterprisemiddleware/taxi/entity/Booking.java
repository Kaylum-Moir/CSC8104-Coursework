package uk.ac.newcastle.enterprisemiddleware.taxi.entity;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Entity
// Adding a constraint to only allow one booking per taxi per date
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"taxi", "date"}))
@Schema(name = "Booking", description = "A Booking that stores Customer, Taxi and Date.")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer")
    private Customer customer;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "taxi")
    private Taxi taxi;

    @NotNull
    @FutureOrPresent // Cannot make a booking for the past
    @Column(name = "date")
    private LocalDate date;

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Taxi getTaxi() {
        return taxi;
    }

    public void setTaxi(Taxi taxi) {
        this.taxi = taxi;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
