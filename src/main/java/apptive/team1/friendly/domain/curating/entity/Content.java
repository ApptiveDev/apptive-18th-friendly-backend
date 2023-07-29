package apptive.team1.friendly.domain.curating.entity;

import apptive.team1.friendly.domain.curating.dto.ContentFormDto;
import apptive.team1.friendly.domain.curating.exception.CanNotPushLikeException;
import apptive.team1.friendly.domain.post.exception.AccessDeniedException;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.global.baseEntity.BaseEntity;
import apptive.team1.friendly.global.common.s3.AwsS3Uploader;
import apptive.team1.friendly.global.common.s3.FileInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Content extends BaseEntity {

    @Builder
    public Content(Account account, String title, List<Image> images, String location, String openingHours, String tel, String instagram, String content, int like) {
        this.account = account;
        this.title = title;
        this.images = images;
        this.location = location;
        this.openingHours = openingHours;
        this.tel = tel;
        this.instagram = instagram;
        this.content = content;
        this.like = like;
    }

    @Id @GeneratedValue
    @Column(name = "content_id")
    private Long id;
    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;
    private String title;
    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();
    private String location;
    private String openingHours;
    private String tel;
    private String instagram;
    @Lob
    private String content;
    private int like;


    //===정적 생성 메서드===//
    public static Content createContent(Account user, ContentFormDto contentFormDto) {
        return Content.builder()
                .account(user)
                .content(contentFormDto.getContent())
                .title(contentFormDto.getTitle())
                .tel(contentFormDto.getTel())
                .instagram(contentFormDto.getInstagram())
                .location(contentFormDto.getLocation())
                .openingHours(contentFormDto.getOpeningHours())
                .like(0)
                .build();
    }

    public void uploadImages(List<MultipartFile> multipartFiles, AwsS3Uploader awsS3Uploader) throws IOException {
        if(multipartFiles.size() > 0) {
            for (MultipartFile multipartFile : multipartFiles) {
                FileInfo uploadFile = awsS3Uploader.upload(multipartFile, "content" + this.id.toString());
                addImage(uploadFile);
            }
        }
    }

    private void addImage(FileInfo uploadFile) {
        Image image = Image.builder()
                .originalFileName(uploadFile.getOriginalFileName())
                .uploadFileUrl(uploadFile.getUploadFileUrl())
                .uploadFilePath(uploadFile.getUploadFilePath())
                .uploadFileName(uploadFile.getUploadFileName())
                .content(this)
                .build();
        this.images.add(image);
    }

    public void deleteImages(Account currentUser, AwsS3Uploader awsS3Uploader) {
        isAuthor(currentUser); // 게시물 권한 확인

        if(this.images.size() > 0) {
            for(int i=images.size()-1; i>=0; i--) {
                awsS3Uploader.delete(this.images.get(i).getOriginalFileName());
                deleteImage(images.get(i));
            }
        }
    }

    private void deleteImage(Image image) {
        this.images.remove(image);
    }

    public void update(Account currentUser, ContentFormDto form) {
        isAuthor(currentUser);
        this.content = form.getContent();
        this.instagram = form.getInstagram();
        this.location = form.getLocation();
        this.openingHours = form.getOpeningHours();
        this.tel = form.getTel();
        this.title = form.getTitle();
    }

    public void addLike(Account currentUser) {
        canPushLike(currentUser);
        this.like += 1;
    }

    private void canPushLike(Account currentUser) {
        if(this.account.getId() == currentUser.getId()) {
            throw new CanNotPushLikeException("본인 게시물은 좋아요를 누를 수 없습니다.");
        }
    }

    private void isAuthor(Account currentUser) {
        if(this.account.getId() != currentUser.getId()) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
    }
}