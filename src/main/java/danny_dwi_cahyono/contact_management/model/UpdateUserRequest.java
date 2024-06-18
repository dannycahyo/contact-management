package danny_dwi_cahyono.contact_management.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {
    @Size(min = 3, max = 50)
    private String name;

    @Size(min = 3, max = 50)
    private String password;
}
