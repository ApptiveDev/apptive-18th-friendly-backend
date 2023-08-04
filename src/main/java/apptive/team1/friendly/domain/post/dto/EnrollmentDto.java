package apptive.team1.friendly.domain.post.dto;

import apptive.team1.friendly.domain.user.data.dto.UserInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollmentDto {

    private Long enrollmentId;

    private Long postId;

    private UserInfo applicant;
}
