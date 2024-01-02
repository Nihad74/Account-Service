package com.accountservice.Controller;

import com.accountservice.Service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
public class PaymentController {


    private PaymentService paymentService;

    @GetMapping("/empl/payment")
    public ResponseEntity<?> getPayrolls(){
        return null;
    }

    @PostMapping("/admin/payments")
    public ResponseEntity<?> addPayroll(){
        return null;
    }

    @PutMapping("/acct/payments")
    public ResponseEntity<?> updatePayroll(){
        return null;
    }
}
