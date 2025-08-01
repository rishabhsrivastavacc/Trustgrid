package com.trustgrid.repository;

import com.trustgrid.model.Borrower;
import com.trustgrid.model.LoanApplication;
import com.trustgrid.model.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    List<LoanApplication> findByBorrowerAndStatusIn(Borrower borrower, List<LoanStatus> statuses);
    List<LoanApplication> findByBorrower(Borrower borrower);

}
