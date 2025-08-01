package com.trustgrid.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LoanApplicationResponse {
    private Long id;
    private Double amount;
    private String status;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appliedOn;
    private String lender;
    private boolean alertGenerated;
}
