package apptive.team1.friendly.domain.curating.dto;

import apptive.team1.friendly.domain.curating.entity.Image;
import lombok.Builder;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import java.util.List;

@Data
public class ContentDto {
    @Builder
    public ContentDto(String title, List<Image> images, String location, String openingHours, String tel, String instagram, String content, int like) {
        this.title = title;
        this.images = images;
        this.location = location;
        this.openingHours = openingHours;
        this.tel = tel;
        this.instagram = instagram;
        this.content = content;
        this.like = like;
    }

    private String title;
    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;
    private String location;
    private String openingHours;
    private String tel;
    private String instagram;
    @Lob
    private String content;
    private int like;

    //==정적 생성 메서드==//
    public static ContentDto create(String title, List<Image> images,
                              String location, String instagram,
                              String openingHours, String tel, String content, int like) {
        return ContentDto.builder()
                .content(content)
                .instagram(instagram)
                .images(images)
                .title(title)
                .location(location)
                .openingHours(openingHours)
                .tel(tel)
                .like(like)
                .build();
    }
}
