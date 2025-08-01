package com.trustgrid;

import com.trustgrid.model.Borrower;
import com.trustgrid.repository.BorrowerRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    @Autowired
    private BorrowerRepository borrowerRepository;

    @PostConstruct
    public void init() {
        if (borrowerRepository.findByPan("ABCDE1234F").isEmpty()) {
            Borrower john = new Borrower();
            john.setName("John Doe");
            john.setPan("ABCDE1234F");
            john.setCreditLimit(1000000.0); // 10 lakh
            borrowerRepository.save(john);
        }
        if (borrowerRepository.findByPan("XYZAB5678K").isEmpty()) {
            Borrower jane = new Borrower();
            jane.setName("Jane Smith");
            jane.setPan("XYZAB5678K");
            jane.setCreditLimit(500000.0); // 5 lakh
            borrowerRepository.save(jane);
        }
    }
}
