package apptive.team1.friendly.domain.post.dto.comment;

import apptive.team1.friendly.domain.user.data.dto.profile.ProfileImgDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    public CommentDto(String username, String text, LocalDateTime createTime) {
        this.username = username;
        this.text = text;
        this.createTime = createTime;
    }

    public CommentDto(Long id, ProfileImgDto profileImgDto, String username, String text, LocalDateTime createTime) {
        this.id = id;
        this.profileImgDto = profileImgDto;
        this.username = username;
        this.text = text;
        this.createTime = createTime;
    }

    private Long id;

    private ProfileImgDto profileImgDto;

    private String username;

    private String text;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createTime;
}
