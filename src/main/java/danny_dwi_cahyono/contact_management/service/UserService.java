package danny_dwi_cahyono.contact_management.service;

import danny_dwi_cahyono.contact_management.entity.User;
import danny_dwi_cahyono.contact_management.model.RegisterUserRequest;
import danny_dwi_cahyono.contact_management.repository.UserRepository;
import danny_dwi_cahyono.contact_management.security.BCrypt;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
}
