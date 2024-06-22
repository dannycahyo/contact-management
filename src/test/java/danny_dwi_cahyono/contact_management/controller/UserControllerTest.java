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
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("testuser");
        request.setPassword("supersecret");
        request.setName("User Test");

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertEquals("User successfully registered", response.getData());
                });
    };

    @Test
    void testRegisterBadRequest() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("testuser");
        request.setPassword("supersecret");
        request.setName("User Test");

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest());
    };

    @Test
    void getUserSuccess() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(BCrypt.hashpw("supersecret", BCrypt.gensalt()));
        user.setName("User Test");
        user.setToken("test token");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
        userRepository.save(user);

        mockMvc.perform(
                get("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test token"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<User> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertEquals("testuser", response.getData().getUsername());
                    assertEquals("User Test", response.getData().getName());
                });
    };

    @Test
    void getUserUnauthorized() throws Exception {
        mockMvc.perform(
                get("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "notfound"))
                .andExpectAll(
                        status().isUnauthorized());
    }

    @Test
    void getUserTokenExpired() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("supersecret", BCrypt.gensalt()));
        user.setName("User Test");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() - 3600000);
        userRepository.save(user);

        mockMvc.perform(
                get("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test"))
                .andExpectAll(
                        status().isUnauthorized());
    }

    @Test
    void updateUserUnauthorized() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest();

        mockMvc.perform(
                patch("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isUnauthorized());
    }

    @Test
    void updateUserSuccess() throws Exception {
        User user = new User();
        user.setUsername("Luffy123");
        user.setPassword(BCrypt.hashpw("DFamily", BCrypt.gensalt()));
        user.setName("Monkey D. Luffy");
        user.setToken("luffy token");
        user.setTokenExpiredAt(System.currentTimeMillis() + 100000000000L);
        userRepository.save(user);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("Danny D Cahyono");
        request.setPassword("kingofpirates");

        mockMvc.perform(
                patch("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "luffy token"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<UserResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertEquals("Luffy123", response.getData().getUsername());
                    assertEquals("Danny D Cahyono", response.getData().getName());

                    User updatedUser = userRepository.findById("Luffy123")
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    assertEquals("Danny D Cahyono", updatedUser.getName());
                    assertTrue(BCrypt.checkpw("kingofpirates", updatedUser.getPassword()));

                });
    }
};
