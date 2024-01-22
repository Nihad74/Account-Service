package Controller;

import account.Entity.Salary;
import account.Service.PaymentService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@Validated
@Slf4j
@RequestMapping("/api")
@Controller
public class PaymentController {


    @Autowired
    private PaymentService paymentService;

    @GetMapping("/empl/payment")
    public ResponseEntity<?> getPayrolls(@RequestParam(required = false)String period){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return paymentService.getPayRolls(username, period);
    }

    @PostMapping("/acct/payments")
    @Transactional
    public ResponseEntity<?> uploadPayment(@Valid @RequestBody List<@Valid Salary> salaries, BindingResult result){
        Map<String, Object> response = getErrorResponse(result);
        if(response != null){
            response.put("path", "/api/acct/payments");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "application/json")
                    .body(response);
        }
        return paymentService.uploadPayment(salaries);
    }

    @PutMapping("/acct/payments")
    public ResponseEntity<?> updatePaymentInformation(@Valid @RequestBody Salary salary, BindingResult result){
        Map<String, Object> response = getErrorResponse(result);

        if(response != null){
            response.put("path", "/api/acct/payments");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "application/json")
                    .body(response);
        }

        return paymentService.updatePaymentInformation(salary);
    }

    private Map<String, Object> getErrorResponse(BindingResult result) {
        if(result.hasErrors()){
            String error = result.getFieldError().getDefaultMessage();
            Map<String, Object> response= new LinkedHashMap<>();
            response.put("timestamp", LocalDateTime.now().toString());
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("error", "Bad Request");
            response.put("message", error);
            return response;
        }
        return null;
    }
}
