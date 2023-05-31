package apptive.team1.friendly.domain.post.dto;

import apptive.team1.friendly.domain.post.entity.Comment;
import apptive.team1.friendly.domain.user.data.entity.profile.AccountLanguage;
import apptive.team1.friendly.domain.user.data.entity.profile.AccountNation;
import apptive.team1.friendly.domain.user.data.entity.profile.Language;
import apptive.team1.friendly.domain.user.data.entity.profile.ProfileImg;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
public class PostDto {

    private Long postId;

    // 방장 정보
    private String firstName;

    private String lastName;

    private AccountNation accountNation;

    private List<AccountLanguage> accountLanguages;

    private ProfileImg profileImg;

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
