package apptive.team1.friendly.domain.user.data.dto;

import apptive.team1.friendly.domain.user.data.dto.profile.ProfileImgDto;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.vo.Language;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
    public UserInfo() {
    }

    @Builder(access = AccessLevel.PROTECTED)
    public UserInfo(String firstName, String lastName, String gender, String nation, String city, List<Language> languages, ProfileImgDto profileImgDto) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.nation = nation;
        this.city = city;
        this.languages = languages;
        this.profileImgDto = profileImgDto;
    }

    private String firstName;

    private String lastName;

    private String gender;

    private String nation;

    private String city;

    private List<Language> languages;

    private ProfileImgDto profileImgDto;

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
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .gender(account.getGender())
                .nation(account.getNation())
                .city(account.getCity())
                .languages(account.getLanguages())
                .profileImgDto(profileImgDto)
                .build();
    }
}