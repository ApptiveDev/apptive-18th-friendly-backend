package apptive.team1.friendly.domain.user.data.dto;

import apptive.team1.friendly.domain.user.data.dto.profile.ProfileImgDto;
import apptive.team1.friendly.domain.user.data.entity.ProfileImg;
import apptive.team1.friendly.domain.user.data.vo.Language;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
    public UserInfo() {
    }

    @Builder
    public UserInfo(String firstName, String lastName, String gender, String nation, String city, List<Language> languages, ProfileImg profileImg, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.nation = nation;
        this.city = city;
        this.languages = languages;
        ProfileImgDto.builder()
                .email(email)
                .uploadFileUrl(profileImg.getUploadFileUrl())
                .uploadFilePath(profileImg.getUploadFilePath())
                .uploadFileName(profileImg.getUploadFileName())
                .originalFileName(profileImg.getOriginalFileName());
    }

    private String firstName;

    private String lastName;

    private String gender;

    private String nation;

    private String city;

    private List<Language> languages;

    private ProfileImgDto profileImgDto;

    public static UserInfo create(String email, String firstName, String lastName, String gender,
                                  String nation, String city, List<Language> languages,
                                  ProfileImg profileImg) {
        return UserInfo.builder()
                .firstName(firstName)
                .lastName(lastName)
                .gender(gender)
                .nation(nation)
                .city(city)
                .languages(languages)
                .profileImg(profileImg)
                .email(email)
                .build();
    }
}
