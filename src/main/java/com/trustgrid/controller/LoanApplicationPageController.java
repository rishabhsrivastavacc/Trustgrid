package com.trustgrid.controller;

import com.trustgrid.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoanApplicationPageController {

    @Autowired
    private LoanService loanService;

    @GetMapping("/loan/apply")
    public String showApplyLoanPage() {
        return "apply-loan";  // Your form page
    }

    @GetMapping("/loan/apply/result")
    public String showResultPage(@RequestParam String message, Model model) {
        model.addAttribute("message", message);
        return "loan-result";  // Result page template to nicely format the message
    }

    // New handler to receive form submission
    @PostMapping("/loan/apply/submit")
    public String handleLoanSubmit(@RequestParam String pan,
                                   @RequestParam double amount,
                                   @RequestParam String lender,
                                   RedirectAttributes redirectAttributes) {
        String responseMessage = loanService.applyLoan(pan, amount, lender);

        // Pass message as redirect attribute
        redirectAttributes.addAttribute("message", responseMessage);
        // Redirect to result page
        return "redirect:/loan/apply/result";
    }
    @PostMapping("/loan/apply")
    @ResponseBody
    public String handleChatBotLoanSubmit(@RequestParam String pan,
                                   @RequestParam double amount,
                                   @RequestParam String lender) {
        String responseMessage = loanService.applyLoan(pan, amount, lender);


        // Redirect to result page
        return responseMessage;
    }
}
