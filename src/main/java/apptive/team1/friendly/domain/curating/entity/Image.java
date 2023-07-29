package apptive.team1.friendly.domain.curating.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image{
    @Builder
    public Image(Content content, String originalFileName, String uploadFileName, String uploadFilePath, String uploadFileUrl) {
        this.content = content;
        this.originalFileName = originalFileName;
        this.uploadFileName = uploadFileName;
        this.uploadFilePath = uploadFilePath;
        this.uploadFileUrl = uploadFileUrl;
    }

    @Id
    @GeneratedValue
    @Column(name="content_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="content_id")
    private Content content;

    private String originalFileName;
    private String uploadFileName;
    private String uploadFilePath;
    private String uploadFileUrl;

}