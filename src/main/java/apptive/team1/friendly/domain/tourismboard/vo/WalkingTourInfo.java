package apptive.team1.friendly.domain.tourismboard.vo;

import javax.persistence.Lob;

public class WalkingTourInfo {
    private String cate2_nm;
    private Float Lat;
    private Float Lng;
    private String place;
    private String title;
    private String subTitle;
    private String trfc_Info; // 교통 정보
    private String main_img_normal;
    private String main_img_thumb;
    @Lob
    private String itemcntnts;


}
