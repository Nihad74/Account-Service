package account.api;

import account.Entity.Roles;
import account.Entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Long id;
    private String name;
    private String lastname;
    private String email;

    private List<String> roles;
    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.lastname = user.getLastName();
        this.email = user.getEmail().toLowerCase();
        this.roles = user.getRoles().stream()
                .map(Roles::getRole)
                .map(Enum::name)
                .collect(Collectors.toList());
        Collections.reverse(this.roles);
    }
}
