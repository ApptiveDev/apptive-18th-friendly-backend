package apptive.team1.friendly.domain.tourismboard.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@Getter
public class ThemeTourism extends Tourism {

    @Lob
    private String trfc_info;

    private String usage_day;

    private String addr1;

    private String homepage_url;

    private String gugun_nm;

    @Override
    public void setTourismType() {
        this.setTourismType(TourismType.THEME);
    }
}
