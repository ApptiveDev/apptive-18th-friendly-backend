package apptive.team1.friendly.domain.user.data.dto.profile;

import apptive.team1.friendly.domain.user.data.entity.profile.*;

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
        ProfileImgDto profileImgDto = new ProfileImgDto();
        profileImgDto.setEmail(profileImg.getAccount().getEmail());
        profileImgDto.setUploadFileName(profileImg.getUploadFileName());
        profileImgDto.setOriginalFileName(profileImg.getOriginalFileName());
        profileImgDto.setUploadFilePath(profileImg.getUploadFilePath());
        profileImgDto.setUploadFileUrl(profileImg.getUploadFileUrl());
        return profileImgDto;
    }
}
