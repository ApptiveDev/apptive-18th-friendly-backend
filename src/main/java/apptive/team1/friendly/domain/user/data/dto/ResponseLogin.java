package apptive.team1.friendly.domain.user.data.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseLogin {

    private String accessToken;
}
