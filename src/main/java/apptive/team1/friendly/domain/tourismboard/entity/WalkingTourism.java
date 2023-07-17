package apptive.team1.friendly.domain.tourismboard.entity;

import lombok.Getter;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@Getter
public class WalkingTourism extends Tourism {
    private String cate2_nm;
    @Lob
    private String trfc_info; // 교통 정보

    @Override
    public void setTourismType() {
        this.setTourismType(TourismType.WALKING);
    }
}
