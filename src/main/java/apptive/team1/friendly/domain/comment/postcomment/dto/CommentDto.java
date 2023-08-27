package apptive.team1.friendly.domain.comment.postcomment.dto;

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

    private String username;

    private String text;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createTime;
}
