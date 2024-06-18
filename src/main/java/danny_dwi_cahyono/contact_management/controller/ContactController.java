package danny_dwi_cahyono.contact_management.controller;

import danny_dwi_cahyono.contact_management.entity.User;
import danny_dwi_cahyono.contact_management.model.ContactResponse;
import danny_dwi_cahyono.contact_management.model.CreateContactRequest;
import danny_dwi_cahyono.contact_management.model.WebResponse;
import danny_dwi_cahyono.contact_management.service.ContactService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

}
