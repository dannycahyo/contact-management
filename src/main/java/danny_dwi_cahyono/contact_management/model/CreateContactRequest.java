package danny_dwi_cahyono.contact_management.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateContactRequest {
    private String firstName;

    private String lastName;

    private String email;

    private String phone;
}
