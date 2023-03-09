package apptive.team1.friendly.domain.user.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestSignUp {

    @NotNull
    @Size(min = 3, max = 50)
    private String username;    // 로그인 id

    @NotNull
    @Size(min = 3, max = 50)
    private String email;  // 이메일

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 50)
    private String password;    // 비밀번호

    @NotNull
    private String firstName;   // 이름

    @NotNull
    private String lastName;    // 성

    private Long nation;    // 국가

    private Long language;  // 언어

    private Long interest;  // 하고 싶은 활동

    private Long favorite;  // 좋아하는 것

    private String introduction;    // 자기소개

    @NotNull
    private Long gender; // 성별
}
