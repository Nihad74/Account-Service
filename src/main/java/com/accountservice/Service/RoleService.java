package account.Service;

import account.Entity.Roles;
import account.Entity.User;
import account.Repository.UserRepository;
import account.api.Role;
import account.api.UpdateRole;
import account.api.UserDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> updateUserRole(UpdateRole updateRole){
        User user = userRepository.findUserByEmailIgnoreCase(updateRole.getUser()).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        if(!updateRole.getRole().equals("ADMINISTRATOR") && !updateRole.getRole().equals("ACCOUNTANT")
                && !updateRole.getRole().equals("USER")){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!");
        }

        if(updateRole.getOperation().equals("GRANT")){

            if(updateRole.getRole().equals("ADMINISTRATOR") && !user.getRoles().contains(new Roles("ROLE_ADMINISTRATOR"))){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
            } else if (updateRole.getRole().equals("ACCOUNTANT") && user.getRoles().contains(new Roles("ROLE_ADMINISTRATOR"))){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
            } else if (updateRole.getRole().equals("USER") && user.getRoles().contains(new Roles("ROLE_ADMINISTRATOR"))){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");

            }


            List<Roles> roles = user.getRoles();
            roles.add(new Roles("ROLE_"+updateRole.getRole()));
        }else if(updateRole.getOperation().equals("REMOVE")) {

            if (user.getRoles().contains(new Roles("ROLE_ADMINISTRATOR"))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
            } else if (user.getRoles().size() == 1 && updateRole.getRole().equals("ACCOUNTANT")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
            } else if (user.getRoles().size() == 1 && updateRole.getRole().equals("USER")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");
            }

                user.getRoles().remove(new Roles("ROLE_" + updateRole.getRole()));
            }


            UserDTO userDTO = new UserDTO(user);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userDTO);

    }
    @Transactional
    public ResponseEntity<?> deleteUser (String email) {
        User deletedUser = userRepository.findUserByEmailIgnoreCase(email).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        if(deletedUser.getRoles().contains(new Roles("ROLE_ADMINISTRATOR"))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }
        userRepository.deleteUserByEmail(deletedUser.getEmail());

        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put("user", deletedUser.getEmail().toLowerCase());
        response.put("status", "Deleted successfully!");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }



    public ResponseEntity<?> getUser() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOS = users.stream()
                .map(UserDTO::new)
                .toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDTOS);
    }
}
