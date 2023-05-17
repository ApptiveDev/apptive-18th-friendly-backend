package apptive.team1.friendly.domain.user.data.dto;

import apptive.team1.friendly.domain.user.data.dto.profile.InterestDto;
import apptive.team1.friendly.domain.user.data.dto.profile.LanguageDto;
import apptive.team1.friendly.domain.user.data.dto.profile.NationDto;
import apptive.team1.friendly.domain.user.data.dto.profile.ProfileImgDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfoResponse {

    private Long id;    // 회원 id

    private String email;  // 이메일

    private String firstName;   // 이름

    private String lastName;    // 성

    private String birthday;    // 생일

    private String gender; // 성별

    private String introduction;    // 자기소개

    private List<LanguageDto> languages;  // 언어

    private List<InterestDto> interests;  // 하고 싶은 활동

    private NationDto nation;    // 국가

    private ProfileImgDto profileImgDto;    // 프로필 이미지

    private Set<String> accountAuthorities = new HashSet<>(); // 권한
}
