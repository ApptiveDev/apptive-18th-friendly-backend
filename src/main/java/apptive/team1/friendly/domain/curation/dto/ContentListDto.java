package apptive.team1.friendly.domain.curation.dto;

import apptive.team1.friendly.domain.curation.entity.Content;
import apptive.team1.friendly.domain.curation.entity.Image;
import apptive.team1.friendly.global.common.s3.ImageDto;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ContentListDto {
    public ContentListDto(Long contentId, Image image) {
        this.contentId = contentId;
        this.image = new ImageDto(image.getOriginalFileName(), image.getUploadFileName(),
                image.getUploadFilePath(), image.getUploadFileUrl());
    }

    private Long contentId;

    private ImageDto image;

    //========정적 생성 메서드========//
    public static List<ContentListDto> create(List<Content> contents) {

        List<ContentListDto> contentListDtos = new ArrayList<>();

        for (Content content : contents) {
            ContentListDto contentListDto = new ContentListDto(content.getId(), content.getImages().get(0));
            contentListDtos.add(contentListDto);
        }

        return contentListDtos;
    }
}
