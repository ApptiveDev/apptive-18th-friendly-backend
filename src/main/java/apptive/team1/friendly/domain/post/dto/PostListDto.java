package apptive.team1.friendly.domain.post.dto;

import lombok.Getter;

import javax.persistence.Column;
import java.util.Date;

@Getter
public class PostListDto {
    // 게시판 리스트에 보이는 필드
    private String title;

    @Column(length = 500)
    private String description;

    private int maxPeople;

    private Date promiseTime;

    private String location;
}
