package apptive.team1.friendly.domain.user.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @NotNull
    @Size(min = 3, max = 50)
    private String email;  // 이메일

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 50)
    private String password;    // 비밀번호

    private String firstName;   // 이름

    private String lastName;    // 성

    private String nation;    // 국가

    private Long gender; // 성별

    private String introduction;    // 자기소개

    private List<String> languages;  // 언어

    private List<Long> languageLevels; // 언어 수준

    private List<String> interests;  // 하고 싶은 활동
}
