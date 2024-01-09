package com.accountservice.Controller;


import com.accountservice.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
@Controller
public class PaymentController {


    @Autowired
    private PaymentService paymentService;

    @GetMapping("/empl/payment")
    public ResponseEntity<?> getPayrolls(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return paymentService.getPayRolls(username);
    }

    @PostMapping("/admin/payments")
    public ResponseEntity<?> makePayroll(){
        return paymentService.makePayrolls();
    }

    @PutMapping("/acct/payments")
    public ResponseEntity<?> updatePaymentInformation(){
        return paymentService.updatePaymentInformation();
    }
}
