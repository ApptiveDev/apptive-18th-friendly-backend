package apptive.team1.friendly.domain.tourismboard.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Tourism {
    @Id @GeneratedValue
    private Long id;
    private Float lat;
    private Float lng;
    private String place;
    private String title;
    private String subtitle;
    private String main_img_normal;
    private String main_img_thumb;
    @Lob
    private String itemcntnts;
    @Enumerated(EnumType.STRING)
    private TourismType tourismType;

    public void setTourismType() {

    }

    protected void setTourismType(TourismType tourismType) {
        this.tourismType = tourismType;
    }
}
