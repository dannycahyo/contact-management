package danny_dwi_cahyono.contact_management.controller;

import danny_dwi_cahyono.contact_management.entity.User;
import danny_dwi_cahyono.contact_management.model.AddressResponse;
import danny_dwi_cahyono.contact_management.model.CreateAddressRequest;
import danny_dwi_cahyono.contact_management.model.UpdateAddressRequest;
import danny_dwi_cahyono.contact_management.model.WebResponse;
import danny_dwi_cahyono.contact_management.service.AddressService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
public class AddressControler {
    private final AddressService addressService;

    AddressControler(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping(path = "/api/contacts/{contactId}/addresses", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<AddressResponse> create(User user,
            @RequestBody CreateAddressRequest request,
            @PathVariable("contactId") String contactId) {
        request.setContactId(contactId);
        AddressResponse addressResponse = addressService.create(user, request);
        return WebResponse.<AddressResponse>builder()
                .data(addressResponse)
                .build();
    }

    @PutMapping(path = "/api/contacts/{contactId}/addresses/{addressId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<AddressResponse> update(User user,
            @RequestBody UpdateAddressRequest request,
            @PathVariable("contactId") String contactId,
            @PathVariable("addressId") String addressId) {
        request.setContactId(contactId);
        request.setAddressId(addressId);
        AddressResponse addressResponse = addressService.update(user, request);
        return WebResponse.<AddressResponse>builder()
                .data(addressResponse)
                .build();
    }

    @GetMapping(path = "/api/contacts/{contactId}/addresses/{addressId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<AddressResponse> get(User user,
            @PathVariable("contactId") String contactId,
            @PathVariable("addressId") String addressId) {
        AddressResponse addressResponse = addressService.get(user, contactId, addressId);
        return WebResponse.<AddressResponse>builder()
                .data(addressResponse)
                .build();
    }

    @GetMapping(path = "/api/contacts/{contactId}/addresses", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<List<AddressResponse>> getAllAddress(User user,
            @PathVariable("contactId") String contactId) {

        List<AddressResponse> addressResponses = addressService.list(user, contactId);

        return WebResponse.<List<AddressResponse>>builder()
                .data(addressResponses)
                .build();
    }

    @DeleteMapping(path = "/api/contacts/{contactId}/addresses/{addressId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> remove(User user,
            @PathVariable("contactId") String contactId,
            @PathVariable("addressId") String addressId) {
        addressService.remove(user, contactId, addressId);
        return WebResponse.<String>builder()
                .data("Address deleted")
                .build();
    }

}
