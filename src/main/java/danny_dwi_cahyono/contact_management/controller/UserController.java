package danny_dwi_cahyono.contact_management.controller;

import danny_dwi_cahyono.contact_management.entity.User;
import danny_dwi_cahyono.contact_management.entity.UserResponse;
import danny_dwi_cahyono.contact_management.model.RegisterUserRequest;
import danny_dwi_cahyono.contact_management.model.UpdateUserRequest;
import danny_dwi_cahyono.contact_management.model.WebResponse;
import danny_dwi_cahyono.contact_management.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<UserResponse> get(User user) {
        UserResponse userResponse = userService.get(user);
        return WebResponse.<UserResponse>builder()
                .data(userResponse)
                .build();
    }

    @PostMapping(path = "/api/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> register(@RequestBody RegisterUserRequest userRequest) {
        userService.register(userRequest);
        return WebResponse.<String>builder()
                .data("User successfully registered")
                .build();
    }

    @PatchMapping(path = "/api/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<UserResponse> update(User user, @RequestBody UpdateUserRequest request) {
        UserResponse userResponse = userService.update(user, request);
        return WebResponse.<UserResponse>builder()
                .data(userResponse)
                .build();
    }
}
