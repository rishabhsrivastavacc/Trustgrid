package com.trustgrid.service;

import com.trustgrid.model.*;
import com.trustgrid.repository.BorrowerRepository;
import com.trustgrid.repository.LoanAlertRepository;
import com.trustgrid.repository.LoanApplicationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LoanService {

    @Autowired
    private BorrowerRepository borrowerRepo;

    @Autowired
    private LoanApplicationRepository loanAppRepo;

    @Autowired
    private LoanAlertRepository loanAlertRepo;

    @Transactional
    public String applyLoan(String pan, double amount, String lender) {
        Borrower borrower = borrowerRepo.findByPan(pan)
                .orElseThrow(() -> new RuntimeException("Borrower not found!"));

        List<LoanApplication> activeLoans = loanAppRepo.findByBorrowerAndStatusIn(borrower,
                Arrays.asList(LoanStatus.PENDING, LoanStatus.APPROVED));

        double totalOutstanding = activeLoans.stream().mapToDouble(LoanApplication::getAmount).sum();
        double creditLimit = borrower.getCreditLimit();

        double newTotal = totalOutstanding + amount;
        double remainingLimit = creditLimit - totalOutstanding;

        LoanApplication application = new LoanApplication();
        application.setAmount(amount);
        application.setBorrower(borrower);
        application.setStatus(LoanStatus.PENDING);
        application.setLender(lender);
        loanAppRepo.save(application);

        if (newTotal > creditLimit) {
            LoanAlert alert = new LoanAlert();
            alert.setBorrower(borrower);
            alert.setLoanApplication(application);
            alert.setAlertType("Stacking Risk");
            alert.setReason(String.format("Credit limit exceeded by ₹%.2f (limit: ₹%.2f, total requested: ₹%.2f)",
                    newTotal - creditLimit, creditLimit, newTotal));
            loanAlertRepo.save(alert);

            return "Loan applied but alert generated: Credit limit exceeded.";
        } else {
            return String.format("Loan applied successfully. Remaining risk-free credit: ₹%.2f", remainingLimit);
        }
    }

    public List<LoanAlert> getAlerts(String pan) {
        Borrower borrower = borrowerRepo.findByPan(pan)
                .orElseThrow(() -> new RuntimeException("Borrower not found!"));
        return loanAlertRepo.findByBorrower(borrower);
    }

    public List<LoanApplication> getLoanHistory(String pan) {
        Borrower borrower = borrowerRepo.findByPan(pan)
                .orElseThrow(() -> new RuntimeException("Borrower not found!"));
        return loanAppRepo.findByBorrowerAndStatusIn(borrower,
                Arrays.asList(LoanStatus.PENDING, LoanStatus.APPROVED, LoanStatus.REJECTED));
    }

    /**
     * Returns a response map with:
     *   - stackAlertGenerated : true if any loan has an alert
     *   - loans : List of LoanApplicationResponse objects for given PAN
     */
    public Map<String, Object> getLoansForPanWithAlertFlag(String pan) {
        Borrower borrower = borrowerRepo.findByPan(pan)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));

        List<LoanApplication> loans = loanAppRepo.findByBorrower(borrower);
        List<LoanAlert> alerts = loanAlertRepo.findByBorrower(borrower);

        Set<Long> alertedLoanIds = alerts.stream()
                .map(alert -> alert.getLoanApplication().getId())
                .collect(Collectors.toSet());

        List<LoanApplicationResponse> loanResponses = loans.stream()
                .map(loan -> new LoanApplicationResponse(
                        loan.getId(),
                        loan.getAmount(),
                        loan.getStatus().name(),
                        loan.getAppliedOn(),
                        loan.getLender(),
                        alertedLoanIds.contains(loan.getId())
                ))
                .collect(Collectors.toList());

        boolean stackAlertGenerated = loanResponses.stream()
                .anyMatch(LoanApplicationResponse::isAlertGenerated);

        // --- Risk-free amount calculation ---
        double totalOutstanding = loans.stream().mapToDouble(LoanApplication::getAmount).sum();
        double riskFreeAmount = borrower.getCreditLimit() - totalOutstanding;
        if (riskFreeAmount < 0) riskFreeAmount = 0;

        Map<String, Object> response = new HashMap<>();
        response.put("stackAlertGenerated", stackAlertGenerated);
        response.put("riskFreeAmount", riskFreeAmount);
        response.put("loans", loanResponses);
        return response;
    }
}
