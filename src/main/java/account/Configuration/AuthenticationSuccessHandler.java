package account.Configuration;

import account.Entity.User;
import account.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

@Configuration
public class AuthenticationSuccessHandler implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private UserRepository userRepository;


    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        User user = userRepository.findUserByEmailIgnoreCase(event.getAuthentication().getName()).orElse(null);
        if(user != null){
            user.setFailedLoginAttempts(0);
            userRepository.save(user);
        }

    }
}
