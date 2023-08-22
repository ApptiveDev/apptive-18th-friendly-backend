package apptive.team1.friendly.domain.tourismboard.dto;

import apptive.team1.friendly.domain.tourismboard.entity.Tourism;
import apptive.team1.friendly.domain.tourismboard.entity.TourismType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(access = AccessLevel.PROTECTED)
public class TourismListDto {

    private Long id;

    private String image;

    private String title;

    private TourismType tourismType;

    public static List<TourismListDto> create(List<Tourism> tourismList) {

        List<TourismListDto> tourismListDtos = new ArrayList<>();

        for (Tourism tourism : tourismList) {
            TourismListDto tourismListDto = TourismListDto.builder()
                    .id(tourism.getId())
                    .image(tourism.getMain_img_thumb())
                    .title(tourism.getTitle())
                    .tourismType(tourism.getTourismType())
                    .build();
            tourismListDtos.add(tourismListDto);
        }

        return tourismListDtos;
    }
}
