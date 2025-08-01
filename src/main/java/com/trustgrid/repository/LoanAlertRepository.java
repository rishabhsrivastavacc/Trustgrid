package com.trustgrid.repository;

import com.trustgrid.model.Borrower;
import com.trustgrid.model.LoanAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanAlertRepository extends JpaRepository<LoanAlert, Long> {
    List<LoanAlert> findByBorrower(Borrower borrower);
}
