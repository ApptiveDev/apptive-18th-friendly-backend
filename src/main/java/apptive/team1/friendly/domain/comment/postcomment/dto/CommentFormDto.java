package apptive.team1.friendly.domain.comment.postcomment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Lob;

@Getter @Setter
@NoArgsConstructor
public class CommentFormDto {

    @Lob
    private String text;
}
