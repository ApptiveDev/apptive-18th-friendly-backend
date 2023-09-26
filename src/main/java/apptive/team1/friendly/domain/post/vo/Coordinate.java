package apptive.team1.friendly.domain.post.vo;

import lombok.Data;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Data
@Embeddable
public class Coordinate {
    private String id;
    private String title;
    private String snippet;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
