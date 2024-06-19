package danny_dwi_cahyono.contact_management.controller;

import danny_dwi_cahyono.contact_management.entity.User;
import danny_dwi_cahyono.contact_management.model.ContactResponse;
import danny_dwi_cahyono.contact_management.model.CreateContactRequest;
import danny_dwi_cahyono.contact_management.model.PagingResponse;
import danny_dwi_cahyono.contact_management.model.SearchContactRequest;
import danny_dwi_cahyono.contact_management.model.WebResponse;
import danny_dwi_cahyono.contact_management.service.ContactService;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping(path = "/api/contacts", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<List<ContactResponse>> getAll(User user,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        SearchContactRequest searchContactRequest = SearchContactRequest.builder()
                .page(page)
                .size(size)
                .name(name)
                .email(email)
                .phone(phone)
                .build();

        Page<ContactResponse> contactResponses = contactService.searchContact(user, searchContactRequest);

        PagingResponse pagingResponse = PagingResponse.builder()
                .currentPage(page)
                .totalPage(contactResponses.getTotalPages())
                .size(size)
                .build();

        return WebResponse.<List<ContactResponse>>builder()
                .data(contactResponses.getContent())
                .paging(pagingResponse)
                .build();
    }

    @PostMapping(path = "/api/contacts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request) {
        ContactResponse contactResponse = contactService.create(user, request);
        return WebResponse.<ContactResponse>builder()
                .data(contactResponse)
                .build();
    }

    @GetMapping(path = "/api/contacts/{contactId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<ContactResponse> getContactDetail(User user, @PathVariable("contactId") String contactId) {
        ContactResponse contactResponse = contactService.getById(user, contactId);
        return WebResponse.<ContactResponse>builder()
                .data(contactResponse)
                .build();
    }

    @PutMapping(path = "/api/contacts/{contactId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<ContactResponse> updateContact(User user, @PathVariable("contactId") String contactId,
            @RequestBody CreateContactRequest request) {
        ContactResponse contactResponse = contactService.updateByID(user, contactId, request);
        return WebResponse.<ContactResponse>builder()
                .data(contactResponse)
                .build();
    }

    @DeleteMapping(path = "/api/contacts/{contactId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> deleteContact(User user, @PathVariable("contactId") String contactId) {
        contactService.deleteByID(user, contactId);
        return WebResponse.<String>builder()
                .data("Contact deleted")
                .build();
    }

}
