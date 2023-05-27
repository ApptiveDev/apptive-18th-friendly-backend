package apptive.team1.friendly.global.auth.social.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleUser {
    private String id;
    private String email;
    private String verified_email;
    private String name;
    private String given_name;
    public String family_name;
    public String locale;
    public String picture;
}
