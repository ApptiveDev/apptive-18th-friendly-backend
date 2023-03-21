package apptive.team1.friendly.domain.user.data.dto;

import lombok.Data;

@Data
public class GoogleLoginRequest {

    private String scope;
    private Long expiresIn;
    private String tokenType;
    private String idToken;
}
