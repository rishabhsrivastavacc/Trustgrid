package com.trustgrid.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Borrower borrower;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    private LocalDateTime appliedOn = LocalDateTime.now();
    private String lender;
}
