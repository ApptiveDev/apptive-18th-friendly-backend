package apptive.team1.friendly.domain.post.dto;

import apptive.team1.friendly.domain.post.entity.Comment;
import apptive.team1.friendly.domain.user.data.dto.PostOwnerInfo;
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
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
public class PostDto {

    private Long postId;

    private PostOwnerInfo postOwnerInfo;

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
