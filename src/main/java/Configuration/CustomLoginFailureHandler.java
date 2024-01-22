package Configuration;

import account.Entity.Roles;
import account.Entity.SecurityEvent;
import account.Entity.User;
import account.Repository.SecurityEventRepository;
import account.Repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;


@Configuration
public class CustomLoginFailureHandler implements AuthenticationFailureHandler,  ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    public static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityEventRepository securityEventRepository;

    private final HttpServletRequest request;

    public CustomLoginFailureHandler (HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        if(exception instanceof LockedException){
            throw new ResponseStatusException(HttpStatus.LOCKED, "User is blocked!");
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials!");
        }

    }

    @Override
    @Transactional
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {

        String username =  event.getAuthentication().getName();
        User user = userRepository.findUserByEmailIgnoreCase(username).orElse(null);


        securityEventRepository.save(new SecurityEvent(LocalDate.now(), "LOGIN_FAILED",
                username.toLowerCase(), request.getRequestURI(), request.getRequestURI()));


        if(user != null && !user.getRoles().contains(new Roles("ROLE_ADMINISTRATOR"))){
            if(user.getFailedLoginAttempts() < MAX_FAILED_LOGIN_ATTEMPTS - 1 ){
                user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
                userRepository.save(user);
            } else {
                user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);

                 if(checkLastFiveLoginAttempts(username)){
                     securityEventRepository.save(new SecurityEvent(LocalDate.now(), "BRUTE_FORCE",
                             user.getEmail().toLowerCase(), request.getRequestURI(), request.getRequestURI()));
                 }
                    user.setBlocked(true);
                    securityEventRepository.save(new SecurityEvent(LocalDate.now(), "LOCK_USER",
                            user.getEmail().toLowerCase(), "Lock user " + user.getEmail().toLowerCase(), request.getRequestURI()));

                userRepository.save(user);
            }
        }
    }

    private boolean checkLastFiveLoginAttempts(String username){
        return securityEventRepository.findLastFiveSecurityEventsByUsername(username.toLowerCase()).stream()
                .allMatch(securityEvent -> securityEvent.getAction().equals("LOGIN_FAILED"));
    }

}
