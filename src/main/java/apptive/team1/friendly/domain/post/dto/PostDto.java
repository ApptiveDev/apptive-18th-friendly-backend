package apptive.team1.friendly.domain.post.dto;

import apptive.team1.friendly.domain.post.entity.PostImage;
import apptive.team1.friendly.domain.user.data.dto.PostOwnerInfo;
import apptive.team1.friendly.domain.post.entity.HashTag;
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

    private List<PostImage> postImage = new ArrayList<>();

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
