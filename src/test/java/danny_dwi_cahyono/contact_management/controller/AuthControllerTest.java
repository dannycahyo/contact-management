package danny_dwi_cahyono.contact_management.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import danny_dwi_cahyono.contact_management.entity.User;
import danny_dwi_cahyono.contact_management.entity.UserResponse;
import danny_dwi_cahyono.contact_management.model.AuthorizedResponse;
import danny_dwi_cahyono.contact_management.model.LoginUserRequest;
import danny_dwi_cahyono.contact_management.model.RegisterUserRequest;
import danny_dwi_cahyono.contact_management.model.UpdateUserRequest;
import danny_dwi_cahyono.contact_management.model.WebResponse;
import danny_dwi_cahyono.contact_management.repository.UserRepository;
import danny_dwi_cahyono.contact_management.security.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // @BeforeEach
    // void setUp() {
    // userRepository.deleteAll();
    // }

    @Test
    void loginFailedUserNotFound() throws Exception {
        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("unregistered");
        request.setPassword("supersecret");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isUnauthorized());
    }

    @Test
    void loginFailedWrongPassword() throws Exception {
        User user = new User();
        user.setName("Rororonoa Zorro");
        user.setUsername("santoryu");
        user.setPassword(BCrypt.hashpw("onigiri", BCrypt.gensalt()));
        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("santoryu");
        request.setPassword("itoriu");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isUnauthorized());
    }

    @Test
    void loginSuccess() throws Exception {
        User user = new User();
        user.setName("God D. Ussop");
        user.setUsername("ussop123");
        user.setPassword(BCrypt.hashpw("nakama123", BCrypt.gensalt()));
        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("ussop123");
        request.setPassword("nakama123");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<AuthorizedResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    assertNull(response.getErrors());
                    assertNotNull(response.getData().getToken());
                    assertNotNull(response.getData().getExpiredAt());

                    User userDb = userRepository.findById("ussop123").orElse(null);
                    assertNotNull(userDb);
                    assertEquals(userDb.getToken(), response.getData().getToken());
                    assertEquals(userDb.getTokenExpiredAt(), response.getData().getExpiredAt());
                });
    }

    @Test
    void logoutSuccess() throws Exception {
        User user = new User();
        user.setUsername("Robin123");
        user.setName("Nico Robin");
        user.setPassword(BCrypt.hashpw("robinforlife", BCrypt.gensalt()));
        user.setToken("logout new token");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000L);
        userRepository.save(user);

        mockMvc.perform(
                delete("/api/auth/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "logout new token"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });
                    assertNull(response.getErrors());
                    assertEquals("Logout success", response.getData());

                    User userDb = userRepository.findById("Robin123").orElse(null);
                    assertNotNull(userDb);
                    assertNull(userDb.getTokenExpiredAt());
                    assertNull(userDb.getToken());
                });
    }
}
