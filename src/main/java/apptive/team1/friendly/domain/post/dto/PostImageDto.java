package apptive.team1.friendly.domain.post.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class PostImageDto {
    private String originalFileName;
    private String uploadFileName;
    private String uploadFilePath;
    private String uploadFileUrl;

    @Builder
    public PostImageDto(String originalFileName, String uploadFileName, String uploadFilePath, String uploadFileUrl) {
        this.originalFileName = originalFileName;
        this.uploadFileName = uploadFileName;
        this.uploadFilePath = uploadFilePath;
        this.uploadFileUrl = uploadFileUrl;
    }
}
