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
import danny_dwi_cahyono.contact_management.entity.Address;
import danny_dwi_cahyono.contact_management.entity.Contact;
import danny_dwi_cahyono.contact_management.entity.User;
import danny_dwi_cahyono.contact_management.model.AddressResponse;
import danny_dwi_cahyono.contact_management.model.CreateAddressRequest;
import danny_dwi_cahyono.contact_management.model.UpdateAddressRequest;
import danny_dwi_cahyono.contact_management.model.WebResponse;
import danny_dwi_cahyono.contact_management.repository.AddressRepository;
import danny_dwi_cahyono.contact_management.repository.ContactRepository;
import danny_dwi_cahyono.contact_management.repository.UserRepository;
import danny_dwi_cahyono.contact_management.security.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerTest {
        @Autowired
        private UserRepository userRepository;

        @Autowired
        private ContactRepository contactRepository;

        @Autowired
        private AddressRepository addressRepository;

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp() {
                addressRepository.deleteAll();
                contactRepository.deleteAll();
                userRepository.deleteAll();

                User user = new User();
                user.setUsername("testuser123");
                user.setPassword(BCrypt.hashpw("testsupersecret", BCrypt.gensalt()));
                user.setName("Test User 123");
                user.setToken("newtoken123");
                user.setTokenExpiredAt(System.currentTimeMillis() + 1000000);
                userRepository.save(user);

                Contact contact = new Contact();
                contact.setId("newContactid");
                contact.setUser(user);
                contact.setFirstName("John123");
                contact.setLastName("Doe123");
                contact.setEmail("johndoe@example.com");
                contact.setPhone("+28 9238423432");
                contactRepository.save(contact);
        }

        @Test
        void createAddressSuccess() throws Exception {
                CreateAddressRequest request = new CreateAddressRequest();
                request.setStreet("Jalan Imam Bonjol");
                request.setCity("Surabaya");
                request.setProvince("Jawa Timur");
                request.setCountry("Indonesia");
                request.setPostalCode("398322");

                mockMvc.perform(
                                post("/api/contacts/newContactid/addresses")
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(request))
                                                .header("X-API-TOKEN", "newtoken123"))
                                .andExpectAll(
                                                status().isOk())
                                .andDo(result -> {
                                        WebResponse<AddressResponse> response = objectMapper
                                                        .readValue(result.getResponse().getContentAsString(),
                                                                        new TypeReference<>() {
                                                                        });
                                        assertNull(response.getErrors());
                                        assertEquals(request.getStreet(), response.getData().getStreet());
                                        assertEquals(request.getCity(), response.getData().getCity());
                                        assertEquals(request.getProvince(), response.getData().getProvince());
                                        assertEquals(request.getCountry(), response.getData().getCountry());
                                        assertEquals(request.getPostalCode(), response.getData().getPostalCode());

                                        assertTrue(addressRepository.existsById(response.getData().getId()));
                                });
        }

        @Test
        void getAddressNotFound() throws Exception {
                mockMvc.perform(
                                get("/api/contacts/newContactid/addresses/notexistaddressId")
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("X-API-TOKEN", "newtoken123"))
                                .andExpectAll(
                                                status().isNotFound());
        }

        @Test
        void getAddressSuccess() throws Exception {
                Contact contact = contactRepository.findById("newContactid").orElseThrow();

                Address address = new Address();
                address.setId("validAddressId");
                address.setContact(contact);
                address.setStreet("Jalan");
                address.setCity("Jakarta");
                address.setProvince("DKI");
                address.setCountry("Indonesia");
                address.setPostalCode("31313");
                addressRepository.save(address);

                mockMvc.perform(
                                get("/api/contacts/newContactid/addresses/validAddressId")
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("X-API-TOKEN", "newtoken123"))
                                .andExpectAll(
                                                status().isOk())
                                .andDo(result -> {
                                        WebResponse<AddressResponse> response = objectMapper
                                                        .readValue(result.getResponse().getContentAsString(),
                                                                        new TypeReference<>() {
                                                                        });
                                        assertNull(response.getErrors());
                                        assertEquals(address.getId(), response.getData().getId());
                                        assertEquals(address.getStreet(), response.getData().getStreet());
                                        assertEquals(address.getCity(), response.getData().getCity());
                                        assertEquals(address.getProvince(), response.getData().getProvince());
                                        assertEquals(address.getCountry(), response.getData().getCountry());
                                        assertEquals(address.getPostalCode(), response.getData().getPostalCode());
                                });
        }

        @Test
        void updateAddressSuccess() throws Exception {
                Contact contact = contactRepository.findById("newContactid").orElseThrow();

                Address address = new Address();
                address.setId("newAddressIdNice");
                address.setContact(contact);
                address.setStreet("Old Street");
                address.setCity("Old City");
                address.setProvince("Old Province");
                address.setCountry("Old Country");
                address.setPostalCode("329323");
                addressRepository.save(address);

                UpdateAddressRequest request = new UpdateAddressRequest();
                request.setStreet("New Street");
                request.setCity("New City");
                request.setProvince("New Province");
                request.setCountry("New Country");
                request.setPostalCode("239822");

                mockMvc.perform(
                                put("/api/contacts/newContactid/addresses/newAddressIdNice")
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(request))
                                                .header("X-API-TOKEN", "newtoken123"))
                                .andExpectAll(
                                                status().isOk())
                                .andDo(result -> {
                                        WebResponse<AddressResponse> response = objectMapper
                                                        .readValue(result.getResponse().getContentAsString(),
                                                                        new TypeReference<>() {
                                                                        });
                                        assertNull(response.getErrors());
                                        assertEquals(request.getStreet(), response.getData().getStreet());
                                        assertEquals(request.getCity(), response.getData().getCity());
                                        assertEquals(request.getProvince(), response.getData().getProvince());
                                        assertEquals(request.getCountry(), response.getData().getCountry());
                                        assertEquals(request.getPostalCode(), response.getData().getPostalCode());

                                        assertTrue(addressRepository.existsById(response.getData().getId()));
                                });
        }

        @Test
        void deleteAddressNotFound() throws Exception {
                mockMvc.perform(
                                delete("/api/contacts/newContactid/addresses/notexistaddressId")
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("X-API-TOKEN", "newtoken123"))
                                .andExpectAll(
                                                status().isNotFound());
        }

        @Test
        void deleteAddressSuccess() throws Exception {
                Contact contact = contactRepository.findById("newContactid").orElseThrow();

                Address address = new Address();
                address.setId("lastAddressId");
                address.setContact(contact);
                address.setStreet("Jalan");
                address.setCity("Jakarta");
                address.setProvince("DKI");
                address.setCountry("Indonesia");
                address.setPostalCode("123123");
                addressRepository.save(address);

                mockMvc.perform(
                                delete("/api/contacts/newContactid/addresses/lastAddressId")
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("X-API-TOKEN", "newtoken123"))
                                .andExpectAll(
                                                status().isOk())
                                .andDo(result -> {
                                        WebResponse<String> response = objectMapper.readValue(
                                                        result.getResponse().getContentAsString(),
                                                        new TypeReference<>() {
                                                        });
                                        assertNull(response.getErrors());
                                        assertEquals("Address deleted", response.getData());

                                        assertFalse(addressRepository.existsById("testuser123"));
                                });
        }

        @Test
        void listAddressNotFound() throws Exception {
                mockMvc.perform(
                                get("/api/contacts/wrong/addresses")
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("X-API-TOKEN", "newtoken123"))
                                .andExpectAll(
                                                status().isNotFound());
        }

        @Test
        void listAddressSuccess() throws Exception {
                Contact contact = contactRepository.findById("newContactid").orElseThrow();

                for (int i = 0; i < 5; i++) {
                        Address address = new Address();
                        address.setId("test-" + i);
                        address.setContact(contact);
                        address.setStreet("Jalan");
                        address.setCity("Jakarta");
                        address.setProvince("DKI");
                        address.setCountry("Indonesia");
                        address.setPostalCode("123123");
                        addressRepository.save(address);
                }

                mockMvc.perform(
                                get("/api/contacts/newContactid/addresses")
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("X-API-TOKEN", "newtoken123"))
                                .andExpectAll(
                                                status().isOk())
                                .andDo(result -> {
                                        WebResponse<List<AddressResponse>> response = objectMapper
                                                        .readValue(result.getResponse().getContentAsString(),
                                                                        new TypeReference<>() {
                                                                        });
                                        assertNull(response.getErrors());
                                        assertEquals(5, response.getData().size());
                                });
        }
}
