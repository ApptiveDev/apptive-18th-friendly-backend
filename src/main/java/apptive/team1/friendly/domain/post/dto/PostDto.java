package apptive.team1.friendly.domain.post.dto;
import apptive.team1.friendly.domain.post.dto.comment.CommentDto;
import apptive.team1.friendly.domain.post.entity.*;
import apptive.team1.friendly.domain.post.entity.comment.Comment;
import apptive.team1.friendly.domain.post.vo.Coordinate;
import apptive.team1.friendly.domain.post.vo.Participant;
import apptive.team1.friendly.domain.user.data.dto.profile.ProfileImgDto;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.entity.ProfileImg;
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
    public PostDto(Long postId, List<ImageDto> postImages, List<Participant> participants, UserInfo authorInfo,
                   String title, Set<HashTag> hashTags, int maxPeople, String description,
                   LocalDate startDate, LocalDate endDate, String location, Set<String> rules,
                   Set<Coordinate> coordinates, List<CommentDto> comments) {
        this.postId = postId;
        this.authorInfo = authorInfo;
        this.title = title;
        this.maxPeople = maxPeople;
        this.description = description;
//        this.promiseTime = promiseTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.coordinates = coordinates;
        this.location = location;
        this.participants = participants;
        this.postImages.addAll(postImages);
        this.comments.addAll(comments);
        this.hashTags.addAll(hashTags);
        this.rules.addAll(rules);
    }

    private Long postId;

    private List<ImageDto> postImages = new ArrayList<>();

    private List<Participant> participants;

    private UserInfo authorInfo;

    private String title;

    private Set<HashTag> hashTags = new HashSet<>();

    private int maxPeople;

    private String description;

//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
//    private LocalDateTime promiseTime;

    private LocalDate startDate;

    private LocalDate endDate;

    Set<Coordinate> coordinates = new HashSet<>();

    private String location;

    private Set<String> rules = new HashSet<>();

    private List<CommentDto> comments = new ArrayList<>();

    public static PostDto createPostDto(Post findPost, Account author, List<Account> participants) {

        UserInfo authorInfo = UserInfo.create(author);

        List<Participant> participantList = new ArrayList<>();
        for (Account participantAccount : participants) { // 참여자 객체 생성 후 저장
            ProfileImg profileImg = participantAccount.getProfileImg();
            ImageDto imageDto = null;
            if(profileImg != null) {
                imageDto = new ImageDto(profileImg.getOriginalFileName(), profileImg.getUploadFileName(), profileImg.getUploadFilePath(), profileImg.getUploadFileUrl());
            }
            Participant participant = new Participant(participantAccount.getId(), imageDto);
            participantList.add(participant);
        }

        List<ImageDto> postImageDtos = new ArrayList<>(); // 게시물 이미지 DTO 리스트
        for (PostImage postImage : findPost.getPostImages()) {
            ImageDto postImageDto = new ImageDto(postImage.getOriginalFileName(), postImage.getUploadFileName(),
                    postImage.getUploadFilePath(), postImage.getUploadFileUrl());
            postImageDtos.add(postImageDto);
        }


        List<CommentDto> commentDtos = new ArrayList<>(); // 게시물 댓글 DTO 리스트
        for (Comment comment : findPost.getComments()) {
            Account commentAuthor = comment.getAccount();

            ProfileImgDto profileImgDto = ProfileImgDto.builder()
                    .email(null)
                    .uploadFileUrl(null)
                    .uploadFilePath(null)
                    .uploadFileName(null)
                    .originalFileName(null)
                    .build();

            if(commentAuthor.getProfileImg() != null) {
                profileImgDto.setEmail(commentAuthor.getEmail());
                profileImgDto.setUploadFileUrl(commentAuthor.getProfileImg().getUploadFileUrl());
                profileImgDto.setUploadFilePath(commentAuthor.getProfileImg().getUploadFilePath());
                profileImgDto.setUploadFileName(commentAuthor.getProfileImg().getUploadFileName());
                profileImgDto.setOriginalFileName(commentAuthor.getProfileImg().getOriginalFileName());
            }

            CommentDto commentDto = new CommentDto(comment.getId(), profileImgDto, commentAuthor.getId(), commentAuthor.getFirstName() + commentAuthor.getLastName(),
                    comment.getText(), comment.getCreatedDate());
            commentDtos.add(commentDto);
        }

        return PostDto.builder()
                .authorInfo(authorInfo)
                .participants(participantList)
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
                .coordinates(findPost.getCoordinates())
                .build();
    }
}