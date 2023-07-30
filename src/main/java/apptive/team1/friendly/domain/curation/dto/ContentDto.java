package apptive.team1.friendly.domain.curation.dto;

import apptive.team1.friendly.domain.curation.entity.Heart;
import apptive.team1.friendly.domain.curation.entity.Image;
import apptive.team1.friendly.domain.user.data.dto.ImageDto;
import apptive.team1.friendly.domain.user.data.entity.Account;
import lombok.Builder;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
public class ContentDto {
    @Builder
    public ContentDto(Long id, Account author, String title, List<Image> images, String location, String openingHours,
                      String tel, String instagram, String content, List<Heart> hearts) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.location = location;
        this.openingHours = openingHours;
        this.tel = tel;
        this.instagram = instagram;
        this.content = content;
        this.hearts = hearts;
        for (Image image : images) {
            ImageDto imageDto = new ImageDto(image.getOriginalFileName(), image.getUploadFileName(),
                    image.getUploadFilePath(), image.getUploadFileUrl());
            this.images.add(imageDto);
        }
    }

    private Long id;

    private Account author;

    private String title;

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageDto> images = new ArrayList<>();

    private String location;

    private String openingHours;

    private String tel;

    private String instagram;

    @Lob
    private String content;

    private List<Heart> hearts;

    //==정적 생성 메서드==//
    public static ContentDto create(Long id, Account author, String title, List<Image> images,
                              String location, String instagram,
                              String openingHours, String tel, String content, List<Heart> hearts) {
        return ContentDto.builder()
                .id(id)
                .author(author)
                .content(content)
                .instagram(instagram)
                .images(images)
                .title(title)
                .location(location)
                .openingHours(openingHours)
                .tel(tel)
                .hearts(hearts)
                .build();
    }
}
