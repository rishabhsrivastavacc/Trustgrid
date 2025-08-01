package com.trustgrid.controller;

import com.trustgrid.model.FinancialSummaryResponse;
import com.trustgrid.model.LoanApplicationResponse;
import com.trustgrid.service.LoanService;
import com.trustgrid.service.StatementAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;
    @Autowired
    private StatementAnalysisService statementAnalysisService;

    @PostMapping("/apply")
    public ResponseEntity<String> applyLoan(@RequestParam String pan,
                                            @RequestParam double amount,
                                            @RequestParam(defaultValue = "Unknown Bank") String lender) {
        String response = loanService.applyLoan(pan, amount, lender);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<?>> getAlerts(@RequestParam String pan) {
        return ResponseEntity.ok(loanService.getAlerts(pan));
    }

    @GetMapping("/history")
    public ResponseEntity<List<?>> getLoanHistory(@RequestParam String pan) {
        return ResponseEntity.ok(loanService.getLoanHistory(pan));
    }

    /**
     * GET /api/loan/list-with-alert?pan={pan}
     * Returns a JSON object containing:
     * - stackAlertGenerated : boolean
     * - loans : list of loans with alert flags
     */
    @GetMapping("/list-with-alert")
    public ResponseEntity<Map<String, Object>> getLoansWithAlertFlag(@RequestParam String pan) {
        return ResponseEntity.ok(loanService.getLoansForPanWithAlertFlag(pan));
    }
    @GetMapping("/listWithAlert")
    public ResponseEntity<Map<String, Object>> getLoansWithAlertFlagAPI(@RequestParam String pan) {
        return ResponseEntity.ok(loanService.getLoansForPanWithAlertFlag(pan));
    }
    @PostMapping("/upload-statement")
    public ResponseEntity<FinancialSummaryResponse> uploadBankStatement(
            @RequestParam("file") MultipartFile file,
            @RequestParam String pan,
            @RequestParam(required = false, defaultValue = "0") double savings,
            @RequestParam(required = false, defaultValue = "0") double assets) {

        FinancialSummaryResponse resp = statementAnalysisService.analyzeStatementAndComputeRiskFree(file, pan, savings, assets);
        return ResponseEntity.ok(resp);
    }

}
