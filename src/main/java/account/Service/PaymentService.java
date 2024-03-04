package account.Service;

import account.Entity.Salary;
import account.Entity.User;
import account.Entity.UserAdapter;
import account.Exception.ErrorResponseUtil;
import account.Repository.SalaryRepository;
import account.Repository.UserRepository;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service("paymentService")
public class PaymentService implements UserDetailsService {


    // Rethink these Autowired, are they really needed?
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SalaryRepository salaryRepository;


    public ResponseEntity<?> getPayRolls(final String username, final String period) {

        if (period != null) {
            return getPayRollForPeriod(username, period);
        }

        // Maybe consider a Comparator or smth like that to establish a specific order
        final List<Salary> salaries = salaryRepository.findAllByEmployeeOrderByDateDateDesc(username);
        final List<Map<String, Object>> response = salaries.stream()
                .map(salary -> createResponseMap(username, Optional.of(salary)))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(200)
                .header("Content-Type", "application/json")
                .body(response);
    }

    @Transactional
    public ResponseEntity<?> uploadPayment(final List<Salary> salaries) {

        salaries.forEach(salary -> {
            final String email = salary.getEmployee();
            final User user = userRepository.findUserByEmailIgnoreCase(email).orElseThrow(RuntimeException::new);
            salary.setEmployee(user.getEmail());
        });

        if (!areDatesInAscendingOrder(salaries)) {
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
    public ResponseEntity<?> updatePaymentInformation(final Salary salary) {

        log.error(salaryRepository.findAll().toString());
        final String email = salary.getEmployee();

        final User user = userRepository.findUserByEmailIgnoreCase(email).orElseThrow(RuntimeException::new);
        salaryRepository.updateSalariesByEmployeeAndDate(user.getEmail(), salary.getDate(), salary.getSalary());

        return ResponseEntity
                .status(200)
                .body(Map.of("status", "Updated successfully!"));
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userRepository
                .findUserByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException(""));
        return new UserAdapter(user);
    }


    public boolean areDatesInAscendingOrder(final List<Salary> salaries) {
        // Break up logic into predicates!
        final Predicate<Integer> isSameEmployee = i -> salaries.get(i).getEmployee().equals(salaries.get(i + 1).getEmployee());
        final Predicate<Integer> dateIsEqualOrAfter = i -> salaries.get(i).getDate().isAfter(salaries.get(i + 1).getDate()) || salaries.get(i).getDate().equals(salaries.get(i + 1).getDate());
        final Predicate<Integer> areDatesInDescendingOrder = isSameEmployee.and(dateIsEqualOrAfter);

        return IntStream.range(0, salaries.size() - 1).noneMatch(areDatesInDescendingOrder::test);
    }

    private ResponseEntity<?> getPayRollForPeriod(final String username, final String period) {
        // this seems excessive

        return Optional.of(period)
                .map(p -> {
                    try {
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

    private Map<String, Object> createResponseMap(final String username, final Optional<Salary> salaryOpt) {
        final Salary salary = salaryOpt.orElseThrow(RuntimeException::new);
        final User user = userRepository.findUserByEmailIgnoreCase(username).orElseThrow(RuntimeException::new);

        final Map<String, Object> response = new LinkedHashMap<>();
        response.put("name", user.getName());
        response.put("lastname", user.getLastName());
        response.put("period", salary.getDate().format(DateTimeFormatter.ofPattern("MMMM-yyyy")));
        response.put("salary", salary.getSalary() / 100 + " dollar(s) " + salary.getSalary() % 100 + " cent(s)");

        return response;
    }
}
