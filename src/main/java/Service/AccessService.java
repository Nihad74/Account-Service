package Service;

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
            securityEventRepository.save(new SecurityEvent(LocalDate.now(), "LOCK_USER", subject.getUsername().toLowerCase(),
                    "Lock user " + user.getEmail().toLowerCase(), "/api/admin/user/access"));

            return ResponseEntity
                    .status(200)
                    .body(Map.of("status", "User "+ user.getEmail() + " locked!"));

        }else if(updateUserAccess.getOperation().equals("UNLOCK")){

            user.setBlocked(false);
            user.setFailedLoginAttempts(0);
            securityEventRepository.save(new SecurityEvent(LocalDate.now(), "UNLOCK_USER", subject.getUsername().toLowerCase(),
                    "Unlock user " + user.getEmail().toLowerCase(), "/api/admin/user/access"));

            return ResponseEntity
                    .status(200)
                    .body(Map.of("status", "User "+ user.getEmail() + " unlocked!"));
        }
        return null;

    }

    public ResponseEntity<?> getSecurityEvents(){
        List<SecurityEvent> securityEvents = securityEventRepository.findAll();
        return ResponseEntity.ok(securityEvents);
    }


    public boolean checkLockedUser(String username) {
        User user = userRepository.findUserByEmailIgnoreCase(username).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        return user.isBlocked();
    }
}
