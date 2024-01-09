package com.accountservice.Configuration;

import com.accountservice.Entity.BreachedPassword;
import com.accountservice.Repository.BreachedPasswordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Set;

@Configuration
public class SecurityConfig  {
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private BreachedPasswordsRepository breachedPasswordsRepository;

    private final Set<String> breachedPassword = Set.of
            ("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
                    "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
                    "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(cfg -> cfg.disable())
                .headers(cfg -> cfg.frameOptions().disable())
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/api/empl/payment").hasAnyRole("USER", "ADMIN", "ACCOUNTANT")
                                .requestMatchers("/api/auth/changepass").hasAnyRole("USER", "ADMIN", "ACCOUNTANT")
                                .requestMatchers("/api/acct/payment").hasRole("ACCOUNTANT")
                                .requestMatchers("/api/admin/*").hasRole("ADMIN")
                                .anyRequest().permitAll()

                )
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(restAuthenticationEntryPoint))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }


    @Bean
    public Set<String> getBreachedPasswords(){
        return  Set.of
                ("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
                        "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
                        "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");
    }

}
