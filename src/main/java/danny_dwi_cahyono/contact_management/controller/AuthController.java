package danny_dwi_cahyono.contact_management.controller;

import danny_dwi_cahyono.contact_management.model.AuthorizedResponse;
import danny_dwi_cahyono.contact_management.model.LoginUserRequest;
import danny_dwi_cahyono.contact_management.model.WebResponse;
import danny_dwi_cahyono.contact_management.service.AuthService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/api/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<AuthorizedResponse> login(@RequestBody LoginUserRequest request) {
        AuthorizedResponse authorizedResponse = authService.login(request);
        return WebResponse.<AuthorizedResponse>builder()
                .data(authorizedResponse)
                .build();
    }

}
