package danny_dwi_cahyono.contact_management.controller;

import danny_dwi_cahyono.contact_management.model.RegisterUserRequest;
import danny_dwi_cahyono.contact_management.model.WebResponse;
import danny_dwi_cahyono.contact_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/api/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> register(@RequestBody RegisterUserRequest userRequest) {
        userService.register(userRequest);
        return WebResponse.<String>builder()
                .data("User successfully registered")
                .build();
    }

}
