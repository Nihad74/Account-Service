package com.accountservice.Service;

import com.accountservice.Entity.Salary;
import com.accountservice.Entity.User;
import com.accountservice.Entity.UserAdapter;
import com.accountservice.Exception.ErrorResponseUtil;
import com.accountservice.Exception.PasswordExistsException;
import com.accountservice.Repository.BreachedPasswordsRepository;
import com.accountservice.Repository.SalaryRepository;
import com.accountservice.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service("paymentService")
public class PaymentService implements UserDetailsService {
    @Autowired
    private BreachedPasswordsRepository breachedPasswordsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SalaryRepository salaryRepository;

    public ResponseEntity<?> getPayRolls(String username, String period){

        if (period != null) {
            return getPayRollForPeriod(username, period);
        }

        List<Salary> salaries = salaryRepository.findAllByEmployeeOrderByDateDateDesc(username);
        List<Map<String, Object>> response = salaries.stream()
                .map(salary -> createResponseMap(username, Optional.of(salary)))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(200)
                .header("Content-Type", "application/json")
                .body(response);
    }

    @Transactional
    public ResponseEntity<?> uploadPayment(List<Salary> salaries){

        salaries.forEach(salary ->{
            String email = salary.getEmployee();
            User user = userRepository.findUserByEmailIgnoreCase(email).orElseThrow(RuntimeException::new);
            salary.setEmployee(user.getEmail());
        });

        if(!areDatesInAscendingOrder(salaries)){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponseUtil.createErrorResponse
                            (HttpStatus.BAD_REQUEST, "Dates are not in ascending order!",
                                    "/api/acct/payments"));
        }


        salaryRepository.saveAll(salaries);
        return ResponseEntity
                .status(200)
                .body(Map.of("status", "Added successfully!"));
    }
    @Transactional
    public ResponseEntity<?> updatePaymentInformation(Salary salary){

        log.error(salaryRepository.findAll().toString());
        String email  = salary.getEmployee();

        User user = userRepository.findUserByEmailIgnoreCase(email).orElseThrow(RuntimeException::new);
        salaryRepository.updateSalariesByEmployeeAndDate(user.getEmail(), salary.getDate(), salary.getSalary());


        return ResponseEntity
                .status(200)
                .body(Map.of("status", "Updated successfully!"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findUserByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException(""));
        if(breachedPasswordsRepository.existsByPassword(user.getPassword())){
            throw new PasswordExistsException();
        }
        return new UserAdapter(user);
    }


    public boolean areDatesInAscendingOrder(List<Salary> salaries) {
        for(int i = 0; i < salaries.size() - 1; i++){
            if(salaries.get(i).getEmployee().equals(salaries.get(i+1).getEmployee())){
                if(salaries.get(i).getDate().isAfter(salaries.get(i+1).getDate()) ||
                        salaries.get(i).getDate().equals(salaries.get(i+1).getDate())){
                    return false;
                }
            }
        }
        return true;
    }

    private ResponseEntity<?> getPayRollForPeriod(String username, String period) {
        return Optional.of(period)
                .map(p -> {
                    try{
                        return YearMonth.parse(p, DateTimeFormatter.ofPattern("MM-yyyy"));
                    } catch (Exception e) {
                       throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong date format!");
                    }
                })
                .map(yearMonth -> salaryRepository.findByEmployeeAndDate(username, yearMonth))
                .map(salary -> createResponseMap(username, salary))
                .map(response -> ResponseEntity
                        .status(200)
                        .header("Content-Type", "application/json")
                        .body(response))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ErrorResponseUtil.createErrorResponse
                                (HttpStatus.BAD_REQUEST, "Wrong date format!",
                                        "/api/empl/payment")));
    }

    private Map<String, Object> createResponseMap(String username, Optional<Salary> salary) {
        Salary salary1 = salary.orElseThrow(RuntimeException::new);
        User user = userRepository.findUserByEmailIgnoreCase(username).orElseThrow(RuntimeException::new);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("name", user.getName());
        response.put("lastname", user.getLastName());
        response.put("period", salary1.getDate().format(DateTimeFormatter.ofPattern("MMMM-yyyy")));
        response.put("salary", salary1.getSalary() / 100 + " dollar(s) " + salary1.getSalary() % 100 + " cent(s)");

        return response;
    }
}
