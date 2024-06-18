package danny_dwi_cahyono.contact_management.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorizedResponse {
    private String token;

    private Long expiredAt;
}
