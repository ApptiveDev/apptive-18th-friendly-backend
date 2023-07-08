package apptive.team1.friendly.domain.post.dto;
import apptive.team1.friendly.domain.post.entity.PostImage;
import apptive.team1.friendly.domain.post.vo.AudioGuide;
import apptive.team1.friendly.domain.user.data.dto.PostOwnerInfo;
import apptive.team1.friendly.domain.post.entity.HashTag;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class PostDto {
    @Builder
    public PostDto(Long postId, List<PostImage> postImage, PostOwnerInfo postOwnerInfo,
                   String title, Set<HashTag> hashTag, int maxPeople, String description,
                   LocalDateTime promiseTime, String location, Set<String> rules,
                   Set<CommentDto> comments, AudioGuide audioGuide) {
        this.postId = postId;
        this.postImage = postImage;
        this.postOwnerInfo = postOwnerInfo;
        this.title = title;
        this.hashTag = hashTag;
        this.maxPeople = maxPeople;
        this.description = description;
        this.promiseTime = promiseTime;
        this.location = location;
        this.rules = rules;
        this.comments = comments;
        this.audioGuide = audioGuide;
    }

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

    private AudioGuide audioGuide;
}
