package uk.ac.newcastle.enterprisemiddleware.taxi.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Entity
// Adding a constraint to only allow one booking per taxi per date
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"taxi", "date"}))
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NotBlank
    @ManyToOne(optional = false)
    @JoinColumn(name = "taxi_id")
    private Taxi taxi;

    @NotBlank
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
