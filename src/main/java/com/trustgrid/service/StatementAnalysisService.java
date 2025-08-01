package com.trustgrid.service;

import com.trustgrid.model.FinancialSummaryResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class StatementAnalysisService {

    public FinancialSummaryResponse analyzeStatementAndComputeRiskFree(
            MultipartFile file,
            String pan,
            double savings,
            double assets) {
        double totalEmi = 0;
        double monthlyIncome = 0;

        try (Reader in = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(in);

            for (CSVRecord record : records) {
                // Example: scan Description/Type columns for "<EMI>" and "<Salary>"
                String description = record.get("Description").toLowerCase();
                double amount = Double.parseDouble(record.get("Amount"));

                if (description.contains("emi")) {
                    totalEmi += amount;
                }
                if (description.contains("salary")) {
                    monthlyIncome += amount;
                }
            }
        } catch (Exception e) {
            return new FinancialSummaryResponse(0, 0, 0, savings, assets,
                    "Could not analyze statement: " + e.getMessage());
        }

        // Simple buffer and risk logic – adapt as needed!
        double buffer = 10000; // e.g., buffer before loan considered "risk free"
        double riskFreeAmount = Math.max(0, monthlyIncome - totalEmi - buffer);

        String remark = String.format("Based on statement, risk-free amount available for loan is ₹%.2f", riskFreeAmount);

        // Include savings/assets in the response for transparency
        return new FinancialSummaryResponse(
                riskFreeAmount,
                totalEmi,
                monthlyIncome,
                savings,
                assets,
                remark
        );
    }
}
