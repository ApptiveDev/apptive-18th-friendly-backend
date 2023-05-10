package apptive.team1.friendly.domain.user.data.dto.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileImgDto {

    private String email;
    private String originalFileName;    // 원본 파일 이름
    private String uploadFileName;      // 업로드 파일 이름
    private String uploadFilePath;      // 업로드 파일 경로
    private String uploadFileUrl;       // 업로드 파일 url
}
