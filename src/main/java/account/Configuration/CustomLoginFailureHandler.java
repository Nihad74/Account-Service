package account.Configuration;

import account.Entity.User;
import account.Repository.UserRepository;
import account.Service.AccessService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccessService accessService;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws ServletException, IOException {
        String email = request.getParameter("email");
        User user = userRepository.findUserByEmailIgnoreCase(email).orElseThrow(() -> new RuntimeException("User not found!"));


        if(!user.isBlocked()){
            if(user.getFailedLoginAttempts() < AccessService.MAX_FAILED_ATTEMPTS - 1) {
                accessService.increaseFailedAttempts(user);
            }else{
                accessService.lock(user, request.getContextPath());
            }
        }


        super.onAuthenticationFailure(request, response, exception);
    }
}
