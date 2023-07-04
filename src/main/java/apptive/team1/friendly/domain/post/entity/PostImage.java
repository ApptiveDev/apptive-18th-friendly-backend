package apptive.team1.friendly.domain.post.entity;

import apptive.team1.friendly.global.common.s3.FileInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
public class PostImage{
    public PostImage() {

    }
    @Builder
    public PostImage(Post post, String originalFileName, String uploadFileName, String uploadFilePath, String uploadFileUrl) {
        this.post = post;
        this.originalFileName = originalFileName;
        this.uploadFileName = uploadFileName;
        this.uploadFilePath = uploadFilePath;
        this.uploadFileUrl = uploadFileUrl;
    }

    @Id @GeneratedValue
    @Column(name="postimage_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    private String originalFileName;
    private String uploadFileName;
    private String uploadFilePath;
    private String uploadFileUrl;

}
