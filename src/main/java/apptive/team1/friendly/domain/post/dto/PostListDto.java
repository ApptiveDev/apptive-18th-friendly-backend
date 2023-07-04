package apptive.team1.friendly.domain.post.dto;

import apptive.team1.friendly.domain.user.data.dto.profile.ProfileImgDto;
import apptive.team1.friendly.domain.post.entity.HashTag;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
public class PostListDto {
    // 게시판 리스트에 보이는 필드
    private Long postId;

    private PostImageDto postImageDto;

    private String title;

    private int maxPeople;

    private LocalDateTime promiseTime;

    private String location;

    private String description;

    private Set<HashTag> hashTag = new HashSet<>();

}
