package apptive.team1.friendly.domain.post.dto;
import apptive.team1.friendly.domain.post.entity.*;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.global.common.s3.ImageDto;
import apptive.team1.friendly.domain.user.data.dto.UserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class PostDto {
    @Builder(access = AccessLevel.PROTECTED)
    public PostDto(Long postId, List<ImageDto> postImages, UserInfo currentUserInfo, UserInfo authorInfo,
                   String title, Set<HashTag> hashTags, int maxPeople, String description,
                   LocalDate startDate, LocalDate endDate, String location, Set<String> rules,
                   List<CommentDto> comments) {
        this.postId = postId;
        this.currentUserInfo = currentUserInfo;
        this.authorInfo = authorInfo;
        this.title = title;
        this.maxPeople = maxPeople;
        this.description = description;
//        this.promiseTime = promiseTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.postImages.addAll(postImages);
        this.comments.addAll(comments);
        this.hashTags.addAll(hashTags);
        this.rules.addAll(rules);
    }

    private Long postId;

    private List<ImageDto> postImages = new ArrayList<>();

    private UserInfo currentUserInfo;

    private UserInfo authorInfo;

    private String title;

    private Set<HashTag> hashTags = new HashSet<>();

    private int maxPeople;

    private String description;

//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
//    private LocalDateTime promiseTime;

    private LocalDate startDate;

    private LocalDate endDate;

    private String location;

    private Set<String> rules = new HashSet<>();

    private List<CommentDto> comments = new ArrayList<>();

    public static PostDto createPostDto(Post findPost, Account currentUser, Account author) {

        UserInfo currentUserInfo = UserInfo.create(currentUser);

        UserInfo authorInfo = UserInfo.create(author);

        List<ImageDto> postImageDtos = new ArrayList<>(); // 게시물 이미지 DTO 리스트
        for (PostImage postImage : findPost.getPostImages()) {
            ImageDto postImageDto = new ImageDto(postImage.getOriginalFileName(), postImage.getUploadFileName(),
                    postImage.getUploadFilePath(), postImage.getUploadFileUrl());
            postImageDtos.add(postImageDto);
        }

        List<CommentDto> commentDtos = new ArrayList<>(); // 게시물 댓글 DTO 리스트
        for (Comment comment : findPost.getComments()) {
            CommentDto commentDto = new CommentDto(comment.getAccount().getFirstName() + comment.getAccount().getLastName(),
                    comment.getText(), comment.getCreatedDate());
            commentDtos.add(commentDto);
        }

        return PostDto.builder()
                .currentUserInfo(currentUserInfo)
                .authorInfo(authorInfo)
                .postId(findPost.getId())
                .title(findPost.getTitle())
                .maxPeople(findPost.getMaxPeople())
                .description(findPost.getDescription())
                .hashTags(findPost.getHashTags())
//                .promiseTime(findPost.getPromiseTime())
                .startDate(findPost.getStartDate())
                .endDate(findPost.getEndDate())
                .comments(commentDtos)
                .rules(findPost.getRules())
                .postImages(postImageDtos)
                .location(findPost.getLocation())
                .build();
    }
}