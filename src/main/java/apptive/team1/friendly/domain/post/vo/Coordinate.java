package apptive.team1.friendly.domain.post.vo;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Coordinate {
    private Double latitude;
    private Double longitude;
}
