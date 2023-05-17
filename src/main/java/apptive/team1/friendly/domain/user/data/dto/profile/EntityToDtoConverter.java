package apptive.team1.friendly.domain.user.data.dto.profile;

import apptive.team1.friendly.domain.user.data.dto.AccountInfoResponse;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.entity.profile.*;

import java.util.List;
import java.util.stream.Collectors;

public class EntityToDtoConverter {
    public static InterestDto interestToInterestDto(AccountInterest accountInterest) {
        Interest interest = accountInterest.getInterest();
        InterestDto interestDto = new InterestDto();
        interestDto.setId(interest.getId());
        interestDto.setName(interest.getName());
        return interestDto;
    }

    public static LanguageDto languageToLanguageDto(AccountLanguage accountLanguage) {
        Language language = accountLanguage.getLanguage();
        LanguageDto languageDto = new LanguageDto();
        languageDto.setId(language.getId());
        languageDto.setName(language.getName());
        languageDto.setLevel(accountLanguage.getLevel().getName());
        return languageDto;
    }

    public static NationDto nationToNationDto(AccountNation accountNation) {
        if (accountNation == null) {
            return null;
        }
        Nation nation = accountNation.getNation();
        NationDto nationDto = new NationDto();
        nationDto.setId(nation.getId());
        nationDto.setName(nation.getName());
        nationDto.setCity(accountNation.getCity());
        return nationDto;
    }

    public static ProfileImgDto profileImgToProfileImgDto(ProfileImg profileImg) {
        if (profileImg == null) return null;

        ProfileImgDto profileImgDto = new ProfileImgDto();
        profileImgDto.setEmail(profileImg.getAccount().getEmail());
        profileImgDto.setUploadFileName(profileImg.getUploadFileName());
        profileImgDto.setOriginalFileName(profileImg.getOriginalFileName());
        profileImgDto.setUploadFilePath(profileImg.getUploadFilePath());
        profileImgDto.setUploadFileUrl(profileImg.getUploadFileUrl());
        return profileImgDto;
    }

    public static AccountInfoResponse accountToInfoDto(Account account,
                                                       List<AccountInterest> accountInterests,
                                                       List<AccountLanguage> accountLanguages,
                                                       ProfileImg profileImg,
                                                       AccountNation accountNation) {

        return AccountInfoResponse.builder()
                .id(account.getId())
                .email(account.getEmail())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .birthday(account.getBirthday())
                .gender(account.getGender().getName())
                .introduction(account.getIntroduction())
                .interests(accountInterests.stream()
                        .map(EntityToDtoConverter::interestToInterestDto)
                        .collect(Collectors.toList()))
                .languages(accountLanguages.stream()
                        .map(EntityToDtoConverter::languageToLanguageDto)
                        .collect(Collectors.toList()))
                .profileImgDto(EntityToDtoConverter.profileImgToProfileImgDto(profileImg))
                .nation(EntityToDtoConverter.nationToNationDto(accountNation))
                .accountAuthorities(account.getAccountAuthorities().stream()
                        .map(accountAuthority -> accountAuthority.getAuthority().getAuthorityName())
                        .collect(Collectors.toSet()))
                .build();
    }
}
