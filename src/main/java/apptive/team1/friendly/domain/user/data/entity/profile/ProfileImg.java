package apptive.team1.friendly.domain.user.data.entity.profile;

import apptive.team1.friendly.common.s3.FileInfo;
import apptive.team1.friendly.domain.user.data.entity.Account;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Account account;

    private String originalFileName;    // 원본 파일 이름
    private String uploadFileName;      // 업로드 파일 이름
    private String uploadFilePath;      // 업로드 파일 경로
    private String uploadFileUrl;       // 업로드 파일 url

    public static ProfileImg of(Account account, FileInfo fileInfo) {
        return ProfileImg.builder()
                .account(account)
                .originalFileName(fileInfo.getOriginalFileName())
                .uploadFileName(fileInfo.getUploadFileName())
                .uploadFilePath(fileInfo.getUploadFilePath())
                .uploadFileUrl(fileInfo.getUploadFileUrl())
                .build();
    }
}
