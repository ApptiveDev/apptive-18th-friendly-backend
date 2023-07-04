package apptive.team1.friendly.domain.post.dto;

import lombok.Data;

@Data
public class PostImageDto {
    private String originalFileName;
    private String uploadFileName;
    private String uploadFilePath;
    private String uploadFileUrl;
}
