package danny_dwi_cahyono.contact_management.service;

import danny_dwi_cahyono.contact_management.entity.Address;
import danny_dwi_cahyono.contact_management.entity.Contact;
import danny_dwi_cahyono.contact_management.entity.User;
import danny_dwi_cahyono.contact_management.model.AddressResponse;
import danny_dwi_cahyono.contact_management.model.CreateAddressRequest;
import danny_dwi_cahyono.contact_management.model.UpdateAddressRequest;
import danny_dwi_cahyono.contact_management.repository.AddressRepository;
import danny_dwi_cahyono.contact_management.repository.ContactRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.List;

@Service
public class AddressService {
    AddressRepository addressRepository;
    ContactRepository contactRepository;
    ValidationService validationService;

    public AddressService(AddressRepository addressRepository, ContactRepository contactRepository,
            ValidationService validationService) {
        this.addressRepository = addressRepository;
        this.contactRepository = contactRepository;
        this.validationService = validationService;
    }

    @Transactional
    public AddressResponse create(User user, CreateAddressRequest request) {
        validationService.validate(request);

        Contact contact = contactRepository.findFirstByUserAndId(user, request.getContactId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setContact(contact);
        address.setCity(request.getCity());
        address.setStreet(request.getStreet());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());

        addressRepository.save(address);

        return mapToAddressResponse(address);
    }

    @Transactional
    public AddressResponse update(User user, UpdateAddressRequest request) {
        validationService.validate(request);

        Contact contact = contactRepository.findFirstByUserAndId(user, request.getContactId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = addressRepository.findFirstByContactAndId(contact, request.getAddressId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        address.setCity(request.getCity());
        address.setStreet(request.getStreet());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());

        addressRepository.save(address);

        return mapToAddressResponse(address);
    }

    @Transactional(readOnly = true)
    public AddressResponse get(User user, String contactId, String addressId) {
        Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = addressRepository.findFirstByContactAndId(contact, addressId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        return mapToAddressResponse(address);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> list(User user,
            String contactId) {
        Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        List<Address> addresses = addressRepository.findAllByContact(contact);

        return addresses.stream()
                .map(this::mapToAddressResponse)
                .toList();
    }

    @Transactional
    public void remove(User user, String contactId, String addressId) {
        Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = addressRepository.findFirstByContactAndId(contact, addressId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        addressRepository.delete(address);
    }

    private AddressResponse mapToAddressResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .province(address.getProvince())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .build();
    }
}
