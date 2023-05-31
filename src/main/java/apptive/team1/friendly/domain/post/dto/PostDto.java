package apptive.team1.friendly.domain.post.dto;

import apptive.team1.friendly.domain.post.entity.Comment;
import apptive.team1.friendly.domain.user.data.dto.profile.LanguageDto;
import apptive.team1.friendly.domain.user.data.dto.profile.NationDto;
import apptive.team1.friendly.domain.user.data.dto.profile.ProfileImgDto;
import apptive.team1.friendly.domain.user.data.entity.profile.*;
import apptive.team1.friendly.global.common.s3.FileInfo;
import apptive.team1.friendly.domain.post.entity.HashTag;
import apptive.team1.friendly.domain.user.data.dto.AccountInfoResponse;
import apptive.team1.friendly.domain.user.data.entity.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
public class PostDto {

    private Long postId;

    // 방장 정보
    private String firstName;

    private String lastName;

//    private Gender gender; // gender를 클래스로 쓸건지

    private NationDto nationDto;

    private List<LanguageDto> languageDtoList;

    private ProfileImgDto profileImgDto;

    private String title;

    private Set<HashTag> hashTag;

    private int maxPeople;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime promiseTime;

    private String location;

    private Set<String> rules;

    private Set<CommentDto> comments;

}
