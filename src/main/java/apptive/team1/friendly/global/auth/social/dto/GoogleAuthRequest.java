package apptive.team1.friendly.global.auth.social.dto;

import lombok.Data;

@Data
public class GoogleAuthRequest {

    private String scope;
    private int expires_in;
    private String token_type;
    private String id_token;
    private String access_token;
}
