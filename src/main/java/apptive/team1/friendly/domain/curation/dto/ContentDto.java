package apptive.team1.friendly.domain.curation.dto;

import apptive.team1.friendly.domain.curation.entity.Image;
import apptive.team1.friendly.domain.user.data.dto.ImageDto;
import apptive.team1.friendly.domain.user.data.dto.UserInfo;
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
    public ContentDto(Long id, UserInfo authorInfo, String title, List<Image> images, String location, String openingHours,
                      String tel, String instagram, String content, int likeCount, boolean isCurrentUserPushLike) {
        this.id = id;
        this.authorInfo = authorInfo;
        this.title = title;
        this.location = location;
        this.openingHours = openingHours;
        this.tel = tel;
        this.instagram = instagram;
        this.content = content;
        this.likeCount = likeCount;
        this.isCurrentUserPushLike = isCurrentUserPushLike;

        for (Image image : images) {
            ImageDto imageDto = new ImageDto(image.getOriginalFileName(), image.getUploadFileName(),
                    image.getUploadFilePath(), image.getUploadFileUrl());
            this.images.add(imageDto);
        }
    }

    private Long id;

    private UserInfo authorInfo;

    private String title;

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageDto> images = new ArrayList<>();

    private String location;

    private String openingHours;

    private String tel;

    private String instagram;

    @Lob
    private String content;

    private boolean isCurrentUserPushLike;

    private int likeCount;

    //==정적 생성 메서드==//
    public static ContentDto create(Long id, UserInfo authorInfo, String title, List<Image> images,
                              String location, String instagram,
                              String openingHours, String tel, String content, int likeCount, boolean isCurrentUserPushLike) {
        return ContentDto.builder()
                .id(id)
                .authorInfo(authorInfo)
                .content(content)
                .instagram(instagram)
                .images(images)
                .title(title)
                .location(location)
                .openingHours(openingHours)
                .tel(tel)
                .likeCount(likeCount)
                .isCurrentUserPushLike(isCurrentUserPushLike)
                .build();
    }
}
