package apptive.team1.friendly.domain.user.data.dto;

import apptive.team1.friendly.domain.user.data.constant.LanguageLevel;
import apptive.team1.friendly.domain.user.data.dto.profile.ProfileImgDto;
import apptive.team1.friendly.domain.user.data.entity.Account;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserInfo {
    public UserInfo() {
    }

    @Builder(access = AccessLevel.PROTECTED)
    public UserInfo(Long id, String email, String firstName,
                    String lastName, String birthday, String affiliation,
                    String gender, String nation, String city, List<String> languages,
                    List<LanguageLevel> languageLevels, List<String> interests, ProfileImgDto profileImgDto) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.affiliation = affiliation;
        this.gender = gender;
        this.nation = nation;
        this.city = city;
        if (languages != null)
            this.languages.addAll(languages);
        if (languageLevels != null)
            this.languageLevels.addAll(languageLevels);
        if (interests != null)
            this.interests.addAll(interests);
        this.profileImgDto = profileImgDto;
    }

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String birthday;

    private String gender;

    private String nation;

    private String city;

//    private List<Language> languages;

    private List<String> languages = new ArrayList<>();

    private List<LanguageLevel> languageLevels = new ArrayList<>();

    private List<String> interests = new ArrayList<>();

    private ProfileImgDto profileImgDto;

    private String affiliation;

    public static UserInfo create(Account account) {

        ProfileImgDto profileImgDto = ProfileImgDto.builder()
                .email(null)
                .uploadFileUrl(null)
                .uploadFilePath(null)
                .uploadFileName(null)
                .originalFileName(null)
                .build();

        if(account.getProfileImg() != null) {
            profileImgDto.setEmail(account.getEmail());
            profileImgDto.setUploadFileUrl(account.getProfileImg().getUploadFileUrl());
            profileImgDto.setUploadFilePath(account.getProfileImg().getUploadFilePath());
            profileImgDto.setUploadFileName(account.getProfileImg().getUploadFileName());
            profileImgDto.setOriginalFileName(account.getProfileImg().getOriginalFileName());
        }

        return UserInfo.builder()
                .id(account.getId())
                .email(account.getEmail())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .birthday(account.getBirthday())
                .gender(account.getGender())
                .nation(account.getNation())
                .city(account.getCity())
                .languages(account.getLanguages())
                .languageLevels(account.getLanguageLevels())
                .interests(account.getInterests())
                .profileImgDto(profileImgDto)
                .affiliation(account.getAffiliation())
                .build();
    }
}