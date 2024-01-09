package com.accountservice.Service;

import com.accountservice.Entity.User;
import com.accountservice.Entity.UserAdapter;
import com.accountservice.Exception.PasswordExistsException;
import com.accountservice.Repository.BreachedPasswordsRepository;
import com.accountservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class PaymentService implements UserDetailsService {
    @Autowired
    private BreachedPasswordsRepository breachedPasswordsRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> getPayRolls(String username){
        UserAdapter userAdapter = (UserAdapter) loadUserByUsername(username);
        User user = userAdapter.getUser();

        return ResponseEntity
                .status(200)
                .header("Content-Type", "application/json")
                .body(user);
    }


    public ResponseEntity<?> makePayrolls(){
        return null;
    }

    public ResponseEntity<?> updatePaymentInformation(){
        return null;
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
}
