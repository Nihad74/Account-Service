package account.Controller;

import account.Entity.UserAdapter;
import account.Exception.ErrorResponseUtil;
import account.Service.AccessService;
import account.Service.RoleService;
import account.api.UpdateRole;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
@Slf4j
@RequestMapping("/api/admin")
@Controller
public class RoleController {


    @Autowired
    private RoleService roleService;

    @Autowired
    private AccessService accessService;



    @PutMapping("/user/role")
    public ResponseEntity<?> updateUserRole(@RequestBody @Valid UpdateRole updateRole, BindingResult result,
                                            @AuthenticationPrincipal UserAdapter userAdapter){

        if(accessService.checkLockedUser(userAdapter.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is locked!");
        }

        if(result.hasErrors()){
            return ResponseEntity
                    .badRequest()
                    .body(ErrorResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST,
                            Objects.requireNonNull(result.getFieldError()).toString(), "/api/admin/user/role"));
        }

        return roleService.updateUserRole(updateRole);
    }


    @DeleteMapping("/user/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable("email") String email){
        return roleService.deleteUser(email);
    }

    @GetMapping("/user/")
    public ResponseEntity<?> getUser(){
        return roleService.getUser();
    }
}
