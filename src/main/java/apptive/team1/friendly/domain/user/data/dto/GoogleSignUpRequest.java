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

    private String  city;   // 도시
    
    private String gender; // 성별

    private String introduction;    // 자기소개

//    private List<Language> languages;  // 언어

    private List<String> languages;

    private List<String> languageLevels;

    private List<String> interests;  // 하고 싶은 활동

    private String affiliation; // 소속


    @Override
    public String toString() {
        return "GoogleSignUpRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday='" + birthday + '\'' +
                ", nation='" + nation + '\'' +
                ", city='" + city + '\'' +
                ", gender='" + gender + '\'' +
                ", introduction='" + introduction + '\'' +
                ", languages=" + languages +
                ", languageLevels=" + languageLevels +
                ", interests=" + interests +
                ", affiliation='" + affiliation + '\'' +
                '}';
    }
}
