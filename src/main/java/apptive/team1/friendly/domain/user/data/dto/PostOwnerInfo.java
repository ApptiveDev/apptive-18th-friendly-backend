package apptive.team1.friendly.domain.user.data.dto;

import apptive.team1.friendly.domain.user.data.dto.profile.LanguageDto;
import apptive.team1.friendly.domain.user.data.dto.profile.NationDto;
import apptive.team1.friendly.domain.user.data.dto.profile.ProfileImgDto;
import lombok.Data;

import java.util.List;

@Data
public class PostOwnerInfo {
    // 방장 정보
    private String firstName;

    private String lastName;

    private String gender;

    private NationDto nationDto;

    private List<LanguageDto> languageDtoList;

    private ProfileImgDto profileImgDto;
}
