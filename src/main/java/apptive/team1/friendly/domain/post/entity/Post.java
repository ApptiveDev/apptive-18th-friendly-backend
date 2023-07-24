package apptive.team1.friendly.domain.post.entity;

import apptive.team1.friendly.domain.post.dto.PostFormDto;
import apptive.team1.friendly.domain.post.exception.AccessDeniedException;
import apptive.team1.friendly.domain.post.vo.AudioGuide;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.global.baseEntity.BaseEntity;
import apptive.team1.friendly.global.common.s3.AwsS3Uploader;
import apptive.team1.friendly.global.common.s3.FileInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseEntity {

    @Builder
    public Post(String title, String description, int maxPeople,
                LocalDateTime promiseTime, String location, Set<String> rules,
                Set<HashTag> hashTags, LocalDateTime createdDate, AudioGuide audioGuide) {
        this.title = title;
        this.description = description;
        this.maxPeople = maxPeople;
        this.promiseTime = promiseTime;
        this.location = location;
        this.rules = rules;
        this.hashTags = hashTags;
        this.audioGuide = audioGuide;
        this.setCreatedDate(createdDate);
        this.setLastModifiedDate(createdDate);
    }


    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시판 리스트에 보이는 필드
    private String title;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>(); // 게시판 이미지

    @Lob
    private String description;

    private int maxPeople;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime promiseTime;

    private String location;


    // 게시글에 들어가야 보이는 필드

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Set<HashTag> hashTags = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @NotNull
    private Set<String> rules = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @Embedded
    @Nullable
    private AudioGuide audioGuide;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AccountPost> accountPosts = new ArrayList<>();

    //========비즈니스 로직==========//
    /**
     * 게시물의 이미지 리스트를 게시물과 서버에서 삭제
     */
    public void deleteImages(Account currentUser, AwsS3Uploader awsS3Uploader) {
        Account postOwner = accountPosts.get(0).getUser();
        isHasAuthority(currentUser, postOwner); // 본인 게시물 아니면 삭제 불가
        if(this.postImages.size() > 0) {
            for (int i=this.postImages.size()-1; i>=0; i--) {
                awsS3Uploader.delete(this.postImages.get(i).getOriginalFileName());
                deleteImage(this.postImages.get(i));
            }
        }
    }

    /**
     * 이미지 리스트를 서버에 업로드 및 게시물에 추가
     */
    public void uploadImages(List<MultipartFile> multipartFiles, AwsS3Uploader awsS3Uploader) throws IOException {
        if(multipartFiles.size() > 0) {
            for(MultipartFile multipartFile : multipartFiles) {
                FileInfo uploadFile = awsS3Uploader.upload(multipartFile, "post"+ this.getId().toString());
                addImage(uploadFile);
            }
        }
    }

    /**
     * 게시물 수정
     */
    public void update(Account currentUser, PostFormDto formDto) {
        Account postOwner = accountPosts.get(0).getUser();
        isHasAuthority(currentUser, postOwner); // 본인 게시물 아니면 수정 불가
        this.title = formDto.getTitle();
        this.hashTags= formDto.getHashTags();
        this.maxPeople = formDto.getMaxPeople();
        this.description = formDto.getDescription();
        this.promiseTime = formDto.getPromiseTime();
        this.location = formDto.getLocation();
        this.rules = formDto.getRules();
        this.setLastModifiedDate(LocalDateTime.now());
    }

    /**
     * 이미지를 게시물에 추가
     */
    public void addImage(FileInfo uploadFile) {
        PostImage postImage = PostImage.builder()
                .post(this) // 연관관계 설정
                .originalFileName(uploadFile.getOriginalFileName())
                .uploadFileName(uploadFile.getUploadFileName())
                .uploadFilePath(uploadFile.getUploadFileName())
                .uploadFileUrl(uploadFile.getUploadFileUrl())
                .build();
        this.postImages.add(postImage);
    }

    /**
     * 이미지를 게시물에서 삭제
     */
    public void deleteImage(PostImage postImage) {
        this.postImages.remove(postImage);
    }

    /**
     * 여행 참가자 추가
     */
    public void addParticipant(Account currentUser) {
        checkCanParticipant();
        AccountPost accountPost = AccountPost.createAccountPost(currentUser, this, AccountType.PARTICIPANT);
        this.accountPosts.add(accountPost);
    }

    /**
     * 참여가능한 인원수 확인
     */
    private void checkCanParticipant() {
        if(accountPosts.size() >= maxPeople) {
            throw new RuntimeException("인원 초과");
        }
    }

    /**
     * 게시물 수정/삭제 권한 확인
     */
    private void isHasAuthority(Account currentUser, Account author) {
        if(!Objects.equals(author.getId(), currentUser.getId()))
            throw new AccessDeniedException("접근 권한이 없습니다.");
    }

    //========= 정적 메소드 ===========/
    // post 생성
    public static Post createPost(Account author, PostFormDto formDto) {
        Post post = Post.builder()
                .createdDate(LocalDateTime.now())
                .maxPeople(formDto.getMaxPeople())
                .title(formDto.getTitle())
                .promiseTime(formDto.getPromiseTime())
                .description(formDto.getDescription())
                .location(formDto.getLocation())
                .hashTags(formDto.getHashTags())
                .rules(formDto.getRules())
                .audioGuide(formDto.getAudioGuide())
                .build();
        AccountPost accountPost = AccountPost.createAccountPost(author, post, AccountType.AUTHOR);
        post.getAccountPosts().add(accountPost);
        return post;
    }

}
