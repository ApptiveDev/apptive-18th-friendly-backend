package apptive.team1.friendly.domain.tourismboard.dto;

import apptive.team1.friendly.domain.tourismboard.entity.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access= AccessLevel.PROTECTED)
public class TourismDto {

    private Float lat;

    private Float lng;

    private String place;

    private String title;

    private String subtitle;

    private String main_img_normal;

    private String itemcntnts; // 상세 내용

    private String tourismType;

    private String addr1;

    private String gugun_nm; // 구군

    private String trfc_info; // 교통 정보

    private String usage_day_week_and_time;

    private String rprsntv_menu;

    public static TourismDto create(Tourism tourism) {

        TourismDto.TourismDtoBuilder builder = TourismDto.builder()
                .lat(tourism.getLat())
                .lng(tourism.getLng())
                .place(tourism.getPlace())
                .title(tourism.getTitle())
                .subtitle(tourism.getSubtitle())
                .main_img_normal(tourism.getMain_img_normal())
                .itemcntnts(tourism.getItemcntnts())
                .tourismType(TourismType.toKoreanName(tourism.getTourismType()));

        if (tourism instanceof FamousRestaurant) {
            FamousRestaurant restaurant = (FamousRestaurant) tourism;
            builder.gugun_nm(restaurant.getGugun_nm())
                    .usage_day_week_and_time(restaurant.getUsage_day_week_and_time())
                    .rprsntv_menu(restaurant.getRprsntv_menu())
                    .addr1(restaurant.getAddr1());
        }
        else if (tourism instanceof ThemeTourism) {
            ThemeTourism themeTourism = (ThemeTourism) tourism;
            builder.trfc_info(themeTourism.getTrfc_info())
                    .addr1(themeTourism.getAddr1())
                    .gugun_nm(themeTourism.getGugun_nm());
        }
        else if(tourism instanceof WalkingTourism) {
            WalkingTourism walkingTourism = (WalkingTourism) tourism;
            builder.trfc_info(walkingTourism.getTrfc_info());
        }

        return builder.build();
    }
}
