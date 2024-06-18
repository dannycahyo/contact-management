package danny_dwi_cahyono.contact_management.service;

import danny_dwi_cahyono.contact_management.entity.User;
import danny_dwi_cahyono.contact_management.model.AuthorizedResponse;
import danny_dwi_cahyono.contact_management.model.LoginUserRequest;
import danny_dwi_cahyono.contact_management.repository.UserRepository;
import danny_dwi_cahyono.contact_management.security.BCrypt;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UserRepository userRepository;

    private final ValidationService validationService;

    public AuthService(UserRepository userRepository, ValidationService validationService) {
        this.userRepository = userRepository;
        this.validationService = validationService;
    }

    @Transactional
    public AuthorizedResponse login(LoginUserRequest request) {
        validationService.validate(request);

        User user = userRepository.findById(request.getUsername())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiredAt(System.currentTimeMillis() + 3600000); // 1 hour
        userRepository.save(user);

        return AuthorizedResponse.builder()
                .token(user.getToken())
                .expiredAt(user.getTokenExpiredAt())
                .build();
    }

    @Transactional
    public void logout(User user) {
        user.setToken(null);
        user.setTokenExpiredAt(null);
        userRepository.save(user);
    }
}
