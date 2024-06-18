package danny_dwi_cahyono.contact_management.service;

import danny_dwi_cahyono.contact_management.entity.Contact;
import danny_dwi_cahyono.contact_management.entity.User;
import danny_dwi_cahyono.contact_management.model.ContactResponse;
import danny_dwi_cahyono.contact_management.model.CreateContactRequest;
import danny_dwi_cahyono.contact_management.repository.ContactRepository;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ContactService {
    private ContactRepository contactRepository;

    private ValidationService validationService;

    public ContactService(ContactRepository contactRepository, ValidationService validationService) {
        this.contactRepository = contactRepository;
        this.validationService = validationService;
    }

    @Transactional
    public ContactResponse create(User user, CreateContactRequest request) {
        validationService.validate(request);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setUser(user);

        contactRepository.save(contact);

        return mapToResponse(contact);
    }

    @Transactional(readOnly = true)
    public ContactResponse getById(User user, String id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        return mapToResponse(contact);
    }

    @Transactional
    public ContactResponse updateByID(User user, String id, CreateContactRequest request) {
        validationService.validate(request);

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());

        contactRepository.save(contact);

        return mapToResponse(contact);
    }

    private ContactResponse mapToResponse(Contact contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }
}
