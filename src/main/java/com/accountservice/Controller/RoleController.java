package com.accountservice.Controller;

import com.accountservice.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/admin")
@Controller
public class RoleController {


    @Autowired
    private RoleService roleService;

    @PutMapping("/user/role")
    public ResponseEntity<?> updateUserRole(){
        return roleService.updateUserRole();
    }


    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(){
        return roleService.deleteUser();
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(){
        return roleService.getUser();
    }
}
