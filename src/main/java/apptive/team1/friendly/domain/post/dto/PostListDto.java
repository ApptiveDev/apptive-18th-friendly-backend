package apptive.team1.friendly.domain.post.dto;

import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.user.data.dto.profile.ProfileImgDto;
import apptive.team1.friendly.domain.post.entity.HashTag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
public class PostListDto {

    private Long postId;

    private PostImageDto postImageDto;

    private String title;

    private int maxPeople;

//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
//    private LocalDateTime promiseTime;

    private LocalDate startDate;

    private LocalDate endDate;

    private String location;

    private String description;

    private Set<HashTag> hashTags = new HashSet<>();

    @Builder
    public PostListDto(Long postId, PostImageDto postImageDto, String title, int maxPeople, LocalDate startDate, LocalDate endDate, String location, String description, Set<HashTag> hashTags) {
        this.postId = postId;
        this.postImageDto = postImageDto;
        this.title = title;
        this.maxPeople = maxPeople;
//        this.promiseTime = promiseTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.description = description;
        this.hashTags = hashTags;
    }

    public static List<PostListDto> createPostListDto(List<Post> posts) {

        List<PostListDto> postListDtos = new ArrayList<>();
        for(Post post : posts) {
            PostListDto postListDto = PostListDto.builder()
                    .postId(post.getId())
                    .title(post.getTitle())
                    .maxPeople(post.getMaxPeople())
                    .hashTags(post.getHashTags())
//                    .promiseTime(post.getPromiseTime())
                    .startDate(post.getStartDate())
                    .endDate(post.getEndDate())
                    .description(post.getDescription())
                    .location(post.getLocation())
                    .build();

            // 대표 이미지 설정
            if(post.getPostImages().size() > 0) {
                PostImageDto postImageDto = PostImageDto.builder()
                        .originalFileName(post.getPostImages().get(0).getOriginalFileName())
                        .uploadFileUrl(post.getPostImages().get(0).getUploadFileUrl())
                        .uploadFileName(post.getPostImages().get(0).getUploadFileName())
                        .uploadFilePath(post.getPostImages().get(0).getUploadFilePath())
                        .build();
                postListDto.setPostImageDto(postImageDto);
            }
            postListDtos.add(postListDto);

        }

        return postListDtos;
    }
}
