package account.Controller;

import account.Service.AccessService;
import account.api.UpdateUserAccess;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AccessController{

    @Autowired
    private AccessService accessService;


    @PutMapping("/api/admin/user/access")
    public ResponseEntity<?> updateUserAccess(@Valid @RequestBody UpdateUserAccess updateUserAccess) {

        return accessService.updateUserAccess(updateUserAccess);
    }

    @GetMapping("/api/security/events/")
    public ResponseEntity<?> getSecurityEvents(){
        return accessService.getSecurityEvents();
    }
}
