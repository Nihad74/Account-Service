package account.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.Set;

@Configuration

public class SecurityConfig {
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private CustomLoginFailureHandler CustomLoginFailureHandler;



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
                .csrf(AbstractHttpConfigurer::disable)
                .headers(cfg -> cfg.frameOptions().disable())
                .authorizeHttpRequests(authorize -> {
                            try {
                                authorize
                                        .requestMatchers(HttpMethod.POST, "/api/auth/changepass").hasAnyAuthority("ROLE_USER", "ROLE_ADMINISTRATOR", "ROLE_ACCOUNTANT")
                                        .requestMatchers(HttpMethod.GET, "/api/empl/payment").hasAnyAuthority("ROLE_USER", "ROLE_ACCOUNTANT")
                                        .requestMatchers(HttpMethod.PUT, "/api/admin/user/role").hasAnyAuthority("ROLE_ADMINISTRATOR")
                                        .requestMatchers("/api/acct/payments").hasAnyAuthority("ROLE_ACCOUNTANT")
                                        .requestMatchers("/api/admin/**").hasAnyAuthority("ROLE_ADMINISTRATOR")
                                        .requestMatchers("/api/security/**").hasAnyAuthority("ROLE_AUDITOR")
                                        .anyRequest().permitAll();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                )
                .httpBasic(Customizer.withDefaults())
                .httpBasic().authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public Set<String> getBreachedPasswords(){
        return  Set.of
                ("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
                        "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
                        "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");
    }


}
