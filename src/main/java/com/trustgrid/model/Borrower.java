package com.trustgrid.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Borrower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String pan;

    private Double creditLimit; // e.g. 10,00,000 (10 lakhs)
}
