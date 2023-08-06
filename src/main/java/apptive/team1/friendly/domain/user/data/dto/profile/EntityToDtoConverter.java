package apptive.team1.friendly.domain.user.data.dto.profile;

import apptive.team1.friendly.domain.user.data.dto.AccountInfoResponse;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.entity.ProfileImg;
import java.util.stream.Collectors;

public class EntityToDtoConverter {
    public static ProfileImgDto profileImgToProfileImgDto(Account account, ProfileImg profileImg) {
        if(profileImg != null) {
            return ProfileImgDto.builder()
                    .email(account.getEmail())
                    .originalFileName(profileImg.getOriginalFileName())
                    .uploadFileName(profileImg.getUploadFileName())
                    .uploadFilePath(profileImg.getUploadFilePath())
                    .uploadFileUrl(profileImg.getUploadFileUrl())
                    .build();
        }
        return new ProfileImgDto(null, null,null, null, null);
    }

    public static AccountInfoResponse accountToUserInfoDto(Account account) {

        return AccountInfoResponse.builder()
                .id(account.getId())
                .email(account.getEmail())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .birthday(account.getBirthday())
                .gender(account.getGender())
                .introduction(account.getIntroduction())
                .interests(account.getInterests())
                .languages(account.getLanguages())
                .profileImgDto(EntityToDtoConverter.profileImgToProfileImgDto(account, account.getProfileImg()))
                .nation(account.getNation())
                .accountAuthorities(account.getAccountAuthorities().stream()
                        .map(accountAuthority -> accountAuthority.getAuthority().getAuthorityName())
                        .collect(Collectors.toSet()))
                .build();
    }
}
