package account.api;

import account.Entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticationUserResponse {

    private String name;
    private String lastname;
    private String email;
    private List<String> roles;

    public AuthenticationUserResponse(User user){
        this.name = user.getName();
        this.lastname = user.getLastName();
        this.email = user.getEmail();
        this.roles = user.getRoles().stream()
                .map(role -> role.getRole().name())
                .toList();
    }
}
