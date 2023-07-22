package apptive.team1.friendly.domain.post.dto;
import apptive.team1.friendly.domain.post.entity.Comment;
import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.post.entity.PostImage;
import apptive.team1.friendly.domain.post.vo.AudioGuide;
import apptive.team1.friendly.domain.user.data.dto.UserInfo;
import apptive.team1.friendly.domain.post.entity.HashTag;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class PostDto {
    @Builder
    public PostDto(Long postId, List<PostImage> postImage, UserInfo userInfo,
                   String title, Set<HashTag> hashTags, int maxPeople, String description,
                   LocalDateTime promiseTime, String location, Set<String> rules,
                   List<CommentDto> comments, AudioGuide audioGuide) {
        this.postId = postId;
        this.postImage = postImage;
        this.userInfo = userInfo;
        this.title = title;
        this.hashTags = hashTags;
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

    private UserInfo userInfo;

    private String title;

    private Set<HashTag> hashTags;

    private int maxPeople;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime promiseTime;

    private String location;

    private Set<String> rules = new HashSet<>();

    private List<CommentDto> comments = new ArrayList<>();

    private AudioGuide audioGuide;

    public static PostDto createPostDto(Post findPost, UserInfo userInfo) {

        List<Comment> comments = findPost.getComments();
        List<CommentDto> commentDtos = new ArrayList<>();
        // 댓글 목록 설정
        if(comments.size() > 0) {
            for (Comment c : comments) {
                CommentDto commentDto = new CommentDto();
                commentDto.setUsername(c.getAccount().getFirstName() + c.getAccount().getLastName());
                commentDto.setText(c.getText());
                commentDto.setCreateTime(c.getCreatedDate());
                commentDtos.add(commentDto);
            }
        }

        return PostDto.builder()
                .userInfo(userInfo)
                .postId(findPost.getId())
                .title(findPost.getTitle())
                .maxPeople(findPost.getMaxPeople())
                .description(findPost.getDescription())
                .rules(findPost.getRules())
                .hashTags(findPost.getHashTags())
                .promiseTime(findPost.getPromiseTime())
                .audioGuide(findPost.getAudioGuide())
                .comments(commentDtos)
                .build();
    }
}
