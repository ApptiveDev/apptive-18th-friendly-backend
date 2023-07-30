package apptive.team1.friendly.domain.curation.dto;

import apptive.team1.friendly.domain.curation.entity.Content;
import apptive.team1.friendly.domain.curation.entity.Image;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ContentListDto {
    public ContentListDto(List<Image> images) {
        this.images.addAll(images);
    }

    List<Image> images = new ArrayList<>();

    public static List<ContentListDto> create(List<Content> contents) {
        List<ContentListDto> contentListDtos = new ArrayList<>();
        for (Content content : contents) {
            ContentListDto contentListDto = new ContentListDto(content.getImages());
            contentListDtos.add(contentListDto);
        }
        return contentListDtos;
    }
}
