package com.trustgrid.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class LoanAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Borrower borrower;

    @ManyToOne
    private LoanApplication loanApplication;

    private String alertType;

    private String reason;

    private LocalDateTime triggeredOn = LocalDateTime.now();
}
