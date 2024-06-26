package danny_dwi_cahyono.contact_management.service;

import danny_dwi_cahyono.contact_management.entity.User;
import danny_dwi_cahyono.contact_management.entity.UserResponse;
import danny_dwi_cahyono.contact_management.model.RegisterUserRequest;
import danny_dwi_cahyono.contact_management.model.UpdateUserRequest;
import danny_dwi_cahyono.contact_management.repository.UserRepository;
import danny_dwi_cahyono.contact_management.security.BCrypt;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ValidationService validationService;

    public UserService(UserRepository userRepository, ValidationService validationService) {
        this.userRepository = userRepository;
        this.validationService = validationService;
    }

    @Transactional
    public void register(RegisterUserRequest request) {
        validationService.validate(request);

        log.info("Register user request: {}", request);

        if (userRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());

        userRepository.save(user);
    }

    public UserResponse get(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    @Transactional
    public UserResponse update(User user, UpdateUserRequest request) {
        validationService.validate(request);

        log.info("Update user request: {}", request);

        if (Objects.nonNull(request.getName())) {
            user.setName(request.getName());
        }

        if (Objects.nonNull(request.getPassword())) {
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }

        userRepository.save(user);

        log.info("User updated: {}", user);

        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }
}
