package apptive.team1.friendly.domain.post.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity @Getter
public class AudioGuide {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private Float mapX;

    private Float mapY;

    private String audioTitle;

    private String audioUrl;

    @Lob
    private String script;

    private String playTime;
}
