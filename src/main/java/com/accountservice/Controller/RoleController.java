package com.accountservice.Controller;

import com.accountservice.Exception.ErrorResponseUtil;
import com.accountservice.Service.RoleService;
import com.accountservice.api.UpdateRole;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequestMapping("/api/admin")
@Controller
public class RoleController {


    @Autowired
    private RoleService roleService;

    @PutMapping("/user/role")
    public ResponseEntity<?> updateUserRole(@RequestBody @Valid UpdateRole updateRole, BindingResult result){
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
