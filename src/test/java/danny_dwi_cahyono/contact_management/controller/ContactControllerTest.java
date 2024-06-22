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
import danny_dwi_cahyono.contact_management.entity.Contact;
import danny_dwi_cahyono.contact_management.entity.User;
import danny_dwi_cahyono.contact_management.model.ContactResponse;
import danny_dwi_cahyono.contact_management.model.CreateContactRequest;
import danny_dwi_cahyono.contact_management.model.WebResponse;
import danny_dwi_cahyono.contact_management.repository.ContactRepository;
import danny_dwi_cahyono.contact_management.repository.UserRepository;
import danny_dwi_cahyono.contact_management.security.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private ContactRepository contactRepository;

        @Autowired
        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp() {
                contactRepository.deleteAll();
                userRepository.deleteAll();

                User user = new User();
                user.setUsername("Franky Shogun");
                user.setPassword(BCrypt.hashpw("sunny go", BCrypt.gensalt()));
                user.setName("Franky");
                user.setToken("new token");
                user.setTokenExpiredAt(System.currentTimeMillis() + 1000000);
                userRepository.save(user);
        }

        @Test
        void createContactSuccess() throws Exception {
                CreateContactRequest request = new CreateContactRequest();
                request.setFirstName("Tralfargar");
                request.setLastName("D. Law");
                request.setEmail("trafalgardlaw@example.com");
                request.setPhone("+37 383292822382");

                mockMvc.perform(
                                post("/api/contacts")
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(request))
                                                .header("X-API-TOKEN", "new token"))
                                .andExpectAll(
                                                status().isOk())
                                .andDo(result -> {
                                        WebResponse<ContactResponse> response = objectMapper
                                                        .readValue(result.getResponse().getContentAsString(),
                                                                        new TypeReference<>() {
                                                                        });
                                        assertNull(response.getErrors());
                                        assertEquals("Tralfargar", response.getData().getFirstName());
                                        assertEquals("D. Law", response.getData().getLastName());
                                        assertEquals("trafalgardlaw@example.com", response.getData().getEmail());
                                        assertEquals("+37 383292822382", response.getData().getPhone());

                                        assertTrue(contactRepository.existsById(response.getData().getId()));
                                });
        }

        @Test
        void getContactNotFound() throws Exception {
                mockMvc.perform(
                                get("/api/contacts/djjd37372")
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("X-API-TOKEN", "new token"))
                                .andExpectAll(
                                                status().isNotFound());
        }

        @Test
        void getContactSuccess() throws Exception {
                User user = userRepository.findById("Luffy123").orElseThrow();

                Contact contact = new Contact();
                contact.setId(UUID.randomUUID().toString());
                contact.setUser(user);
                contact.setFirstName("Tralfargar");
                contact.setLastName("D. Law");
                contact.setEmail("trafalgardlaw@example.com");
                contact.setPhone("+37 383292822382");
                contactRepository.save(contact);

                mockMvc.perform(
                                get("/api/contacts/" + contact.getId())
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("X-API-TOKEN", "new token"))
                                .andExpectAll(
                                                status().isOk())
                                .andDo(result -> {
                                        WebResponse<ContactResponse> response = objectMapper
                                                        .readValue(result.getResponse().getContentAsString(),
                                                                        new TypeReference<>() {
                                                                        });
                                        assertNull(response.getErrors());

                                        assertEquals(contact.getId(), response.getData().getId());
                                        assertEquals(contact.getFirstName(), response.getData().getFirstName());
                                        assertEquals(contact.getLastName(), response.getData().getLastName());
                                        assertEquals(contact.getEmail(), response.getData().getEmail());
                                        assertEquals(contact.getPhone(), response.getData().getPhone());
                                });
        };

        @Test
        void updateContactSuccess() throws Exception {
                User user = userRepository.findById("Luffy123").orElseThrow();

                Contact contact = new Contact();
                contact.setId(UUID.randomUUID().toString());
                contact.setUser(user);
                contact.setFirstName("Tony");
                contact.setLastName("Chopper");
                contact.setEmail("tonychopper@example.com");
                contact.setPhone("9382982");
                contactRepository.save(contact);

                CreateContactRequest request = new CreateContactRequest();
                request.setFirstName("Thony");
                request.setLastName("Thony Copper");
                request.setEmail("tonycoppercompet@example.com");
                request.setPhone("39823");

                mockMvc.perform(
                                put("/api/contacts/" + contact.getId())
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(request))
                                                .header("X-API-TOKEN", "new token"))
                                .andExpectAll(
                                                status().isOk())
                                .andDo(result -> {
                                        WebResponse<ContactResponse> response = objectMapper
                                                        .readValue(result.getResponse().getContentAsString(),
                                                                        new TypeReference<>() {
                                                                        });
                                        assertNull(response.getErrors());
                                        assertEquals(request.getFirstName(), response.getData().getFirstName());
                                        assertEquals(request.getLastName(), response.getData().getLastName());
                                        assertEquals(request.getEmail(), response.getData().getEmail());
                                        assertEquals(request.getPhone(), response.getData().getPhone());

                                        assertTrue(contactRepository.existsById(response.getData().getId()));
                                });
        }

        @Test
        void deleteContactNotFound() throws Exception {
                mockMvc.perform(
                                delete("/api/contacts/notfoundid13838")
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("X-API-TOKEN", "new token"))
                                .andExpectAll(
                                                status().isNotFound());
        }

        @Test
        void deleteContactSuccess() throws Exception {
                User user = userRepository.findById("Luffy123").orElseThrow();

                Contact contact = new Contact();
                contact.setId(UUID.randomUUID().toString());
                contact.setUser(user);
                contact.setFirstName("Tony");
                contact.setLastName("Chopper");
                contact.setEmail("tonnycoper123@example.com");

                contactRepository.save(contact);

                mockMvc.perform(
                                delete("/api/contacts/" + contact.getId())
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("X-API-TOKEN", "new token"))
                                .andExpectAll(
                                                status().isOk())
                                .andDo(result -> {
                                        WebResponse<String> response = objectMapper
                                                        .readValue(result.getResponse().getContentAsString(),
                                                                        new TypeReference<>() {
                                                                        });
                                        assertNull(response.getErrors());
                                        assertEquals("Contact deleted", response.getData());

                                        assertFalse(contactRepository.existsById(contact.getId()));
                                });
        };

        @Test
        void searchNotFound() throws Exception {
                mockMvc.perform(
                                get("/api/contacts")
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("X-API-TOKEN", "new token"))
                                .andExpectAll(
                                                status().isOk())
                                .andDo(result -> {
                                        WebResponse<List<ContactResponse>> response = objectMapper.readValue(
                                                        result.getResponse().getContentAsString(),
                                                        new TypeReference<>() {
                                                        });
                                        assertNull(response.getErrors());
                                        assertEquals(0, response.getPaging().getCurrentPage());
                                        assertEquals(10, response.getPaging().getSize());
                                });
        }

        @Test
        void searchSuccess() throws Exception {
                User user = userRepository.findById("Franky Shogun").orElseThrow();

                for (int i = 0; i < 100; i++) {
                        Contact contact = new Contact();
                        contact.setId(UUID.randomUUID().toString());
                        contact.setUser(user);
                        contact.setFirstName("Mugiwara " + i);
                        contact.setLastName("No Luffy " + i);
                        contact.setEmail("mugiwaranoluffy@example.com");
                        contact.setPhone("+28 2828493");
                        contactRepository.save(contact);
                }

                mockMvc.perform(
                                get("/api/contacts")
                                                .queryParam("name", "Mugiwara")
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("X-API-TOKEN", "new token"))
                                .andExpectAll(
                                                status().isOk())
                                .andDo(result -> {
                                        WebResponse<List<ContactResponse>> response = objectMapper.readValue(
                                                        result.getResponse().getContentAsString(),
                                                        new TypeReference<>() {
                                                        });
                                        assertNull(response.getErrors());
                                        assertEquals(10, response.getData().size());
                                        assertEquals(10, response.getPaging().getTotalPage());
                                        assertEquals(0, response.getPaging().getCurrentPage());
                                        assertEquals(10, response.getPaging().getSize());
                                });

                mockMvc.perform(
                                get("/api/contacts")
                                                .queryParam("name", "No Luffy")
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("X-API-TOKEN", "new token"))
                                .andExpectAll(
                                                status().isOk())
                                .andDo(result -> {
                                        WebResponse<List<ContactResponse>> response = objectMapper.readValue(
                                                        result.getResponse().getContentAsString(),
                                                        new TypeReference<>() {
                                                        });
                                        assertNull(response.getErrors());
                                        assertEquals(10, response.getData().size());
                                        assertEquals(10, response.getPaging().getTotalPage());
                                        assertEquals(0, response.getPaging().getCurrentPage());
                                        assertEquals(10, response.getPaging().getSize());
                                });

                mockMvc.perform(
                                get("/api/contacts")
                                                .queryParam("email", "mugiwaranoluffy@example.com")
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("X-API-TOKEN", "new token"))
                                .andExpectAll(
                                                status().isOk())
                                .andDo(result -> {
                                        WebResponse<List<ContactResponse>> response = objectMapper.readValue(
                                                        result.getResponse().getContentAsString(),
                                                        new TypeReference<>() {
                                                        });
                                        assertNull(response.getErrors());
                                        assertEquals(10, response.getData().size());
                                        assertEquals(10, response.getPaging().getTotalPage());
                                        assertEquals(0, response.getPaging().getCurrentPage());
                                        assertEquals(10, response.getPaging().getSize());
                                });

                mockMvc.perform(
                                get("/api/contacts")
                                                .queryParam("phone", "2828493")
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("X-API-TOKEN", "new token"))
                                .andExpectAll(
                                                status().isOk())
                                .andDo(result -> {
                                        WebResponse<List<ContactResponse>> response = objectMapper.readValue(
                                                        result.getResponse().getContentAsString(),
                                                        new TypeReference<>() {
                                                        });
                                        assertNull(response.getErrors());
                                        assertEquals(10, response.getData().size());
                                        assertEquals(10, response.getPaging().getTotalPage());
                                        assertEquals(0, response.getPaging().getCurrentPage());
                                        assertEquals(10, response.getPaging().getSize());
                                });

                mockMvc.perform(
                                get("/api/contacts")
                                                .queryParam("phone", "2828493")
                                                .queryParam("page", "1000")
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("X-API-TOKEN", "new token"))
                                .andExpectAll(
                                                status().isOk())
                                .andDo(result -> {
                                        WebResponse<List<ContactResponse>> response = objectMapper.readValue(
                                                        result.getResponse().getContentAsString(),
                                                        new TypeReference<>() {
                                                        });
                                        assertNull(response.getErrors());
                                        assertEquals(0, response.getData().size());
                                        assertEquals(10, response.getPaging().getTotalPage());
                                        assertEquals(1000, response.getPaging().getCurrentPage());
                                        assertEquals(10, response.getPaging().getSize());
                                });
        }
}
