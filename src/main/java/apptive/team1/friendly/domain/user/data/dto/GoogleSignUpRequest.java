package apptive.team1.friendly.domain.user.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoogleSignUpRequest {

    private String firstName;   // 이름

    private String lastName;    // 성

    private String birthday;    // 생일

    private String nation;    // 국가

    private String city;    // 도시

    private String gender; // 성별

    private String introduction;    // 자기소개

    private List<String> languages;  // 언어

    private List<String> languageLevels; // 언어 수준

    private List<String> interests;  // 하고 싶은 활동
}
