package apptive.team1.friendly.domain.curating.dto;

import lombok.Builder;
import lombok.Data;
import javax.persistence.Lob;

@Data
public class ContentFormDto {
    @Builder
    public ContentFormDto(String title, String location, String openingHours, String tel, String instagram, String content) {
        this.title = title;
        this.location = location;
        this.openingHours = openingHours;
        this.tel = tel;
        this.instagram = instagram;
        this.content = content;
    }

    private String title;
    private String location;
    private String openingHours;
    private String tel;
    private String instagram;
    @Lob
    private String content;
}
