package account.Service;

import account.Entity.Roles;
import account.Entity.SecurityEvent;
import account.Entity.User;
import account.Entity.UserAdapter;
import account.Repository.SecurityEventRepository;
import account.Repository.UserRepository;
import account.api.UpdateUserAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class AccessService {

    @Autowired
    private SecurityEventRepository securityEventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentService paymentService;

    public static final int MAX_FAILED_ATTEMPTS = 5;

    public ResponseEntity<?> updateUserAccess(UpdateUserAccess updateUserAccess){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserAdapter subject = (UserAdapter) paymentService.loadUserByUsername(username);

        User user = userRepository.findUserByEmailIgnoreCase(updateUserAccess.getUser()).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        if(user.getRoles().contains(new Roles("ROLE_ADMINISTRATOR"))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't lock the ADMINISTRATOR!");
        }
        
        if(updateUserAccess.getOperation().equals("LOCK")) {

            user.setBlocked(true);
            securityEventRepository.save(new SecurityEvent(LocalDate.now(), "LOCK_USER", subject.getUsername(),
                    "Lock user " + user.getEmail(), "/api/admin/user/access"));

        }else if(updateUserAccess.getOperation().equals("UNLOCK")){

            user.setBlocked(false);
            securityEventRepository.save(new SecurityEvent(LocalDate.now(), "LOCK_USER", subject.getUsername(),
                    "Lock user " + user.getEmail(), "/api/admin/user/access"));

        }

        return ResponseEntity
                .status(200)
                .body(Map.of("status", "User "+ user.getEmail() + " "+ updateUserAccess.getOperation().toLowerCase()));
    }

    public ResponseEntity<?> getSecurityEvents(){
        List<SecurityEvent> securityEvents = securityEventRepository.findAll();
        return ResponseEntity.ok(securityEvents);
    }

    public void increaseFailedAttempts(User user){
        int newFailAttempts = user.getFailedLoginAttempts() + 1;
        userRepository.updateFailedLoginAttempts(newFailAttempts, user.getEmail());
    }

    public void resetFailedAttempts(String email){
        userRepository.updateFailedLoginAttempts(0, email);
    }

    public void lock(User user, String contextPath) {
        user.setBlocked(true);
        securityEventRepository.save(new SecurityEvent(LocalDate.now(), "LOCK_USER",
                user.getEmail(),"Lock user " + user.getEmail(), contextPath ));
        userRepository.save(user);
    }
}
