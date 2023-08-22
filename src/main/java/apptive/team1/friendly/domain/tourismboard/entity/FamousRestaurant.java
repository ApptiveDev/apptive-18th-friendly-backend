package apptive.team1.friendly.domain.tourismboard.entity;

import lombok.Getter;

import javax.persistence.Entity;

@Entity
@Getter
public class FamousRestaurant extends Tourism{

    private String addr1;

    private String addr2;

    private String cntct_tel;

    private String homepage_url;

    private String usage_day_week_and_time;

    private String rprsntv_menu;

    private String gugun_nm;

    public void setTourismType() {
        this.setTourismType(TourismType.RESTAURANT);
    }
}
