package apptive.team1.friendly.domain.post.vo;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
@Getter
public class AudioGuide {

    @Column(name = "audio_id")
    private Long tid;
    @Column(name = "audioLocationName")
    private String title;
    @Column(name = "audioMapX")
    private Float mapX;
    @Column(name = "audioMapY")
    private Float mapY;
    @Column(name = "audioTitle")
    private String audioTitle;
    @Column(name = "audioUrl")
    private String audioUrl;
    @Lob
    @Column(name = "audioScript")
    private String script;
}
