package apptive.team1.friendly.domain.curation.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HeartDto {

    public HeartDto(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
    }

    private Long id;

    private Long userId;
}
