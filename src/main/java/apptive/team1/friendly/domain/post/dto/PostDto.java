package apptive.team1.friendly.domain.post.dto;
import apptive.team1.friendly.domain.post.entity.Comment;
import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.post.entity.PostImage;
import apptive.team1.friendly.domain.post.vo.AudioGuide;
import apptive.team1.friendly.global.common.s3.ImageDto;
import apptive.team1.friendly.domain.user.data.dto.UserInfo;
import apptive.team1.friendly.domain.post.entity.HashTag;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class PostDto {
    @Builder
    public PostDto(Long postId, List<PostImage> postImages, UserInfo authorInfo,
                   String title, Set<HashTag> hashTags, int maxPeople, String description,
                   LocalDate startDate, LocalDate endDate, String location, Set<String> rules,
                   List<Comment> comments, AudioGuide audioGuide) {
        this.postId = postId;
        this.authorInfo = authorInfo;
        this.title = title;
        this.hashTags = hashTags;
        this.maxPeople = maxPeople;
        this.description = description;
//        this.promiseTime = promiseTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.audioGuide = audioGuide;
        for (PostImage postImage : postImages) {
            ImageDto postImageDto = new ImageDto(postImage.getOriginalFileName(), postImage.getUploadFileName(),
                    postImage.getUploadFilePath(), postImage.getUploadFileUrl());
            this.postImages.add(postImageDto);
        }
        for (Comment comment : comments) {
            CommentDto commentDto = new CommentDto(comment.getAccount().getFirstName() + comment.getAccount().getLastName(),
                    comment.getText(), comment.getCreatedDate());
            this.comments.add(commentDto);
        }
        this.rules.addAll(rules);
    }

    private Long postId;

    private List<ImageDto> postImages = new ArrayList<>();

    private UserInfo authorInfo;

    private String title;

    private Set<HashTag> hashTags;

    private int maxPeople;

    private String description;

//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
//    private LocalDateTime promiseTime;

    private LocalDate startDate;

    private LocalDate endDate;

    private String location;

    private Set<String> rules = new HashSet<>();

    private List<CommentDto> comments = new ArrayList<>();

    private AudioGuide audioGuide;

    public static PostDto createPostDto(Post findPost, UserInfo authorInfo) {

        return PostDto.builder()
                .authorInfo(authorInfo)
                .postId(findPost.getId())
                .title(findPost.getTitle())
                .maxPeople(findPost.getMaxPeople())
                .description(findPost.getDescription())
                .hashTags(findPost.getHashTags())
//                .promiseTime(findPost.getPromiseTime())
                .startDate(findPost.getStartDate())
                .endDate(findPost.getEndDate())
                .audioGuide(findPost.getAudioGuide())
                .comments(findPost.getComments())
                .rules(findPost.getRules())
                .postImages(findPost.getPostImages())
                .location(findPost.getLocation())
                .build();
    }
}
