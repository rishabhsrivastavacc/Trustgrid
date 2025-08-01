package com.trustgrid.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinancialSummaryResponse {
    private double riskFreeAmount;
    private double totalEmi;
    private double monthlyIncome;
    private double savings;
    private double assets;
    private String remark;
}
