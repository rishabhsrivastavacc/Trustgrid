package com.trustgrid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FetchLoanController {

    @GetMapping("/loan/details")
    public String fetchLoanDetailsPage() {
        return "fetch-loan";  // Thymeleaf template name (fetch-loan.html)
    }
}
