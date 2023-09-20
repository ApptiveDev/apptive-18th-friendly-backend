package apptive.team1.friendly.domain.post.vo;

import apptive.team1.friendly.global.common.s3.ImageDto;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Participant {

    public Participant(Long id, ImageDto profileImg) {
        this.id = id;
        this.profileImgDto = profileImg;
    }

    private Long id;

    private ImageDto profileImgDto;
}
