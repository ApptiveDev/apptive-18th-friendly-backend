package apptive.team1.friendly.domain.post.dto;

import apptive.team1.friendly.domain.post.entity.AccountPost;
import apptive.team1.friendly.domain.post.entity.HashTag;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class PostDto {

    // 게시판 리스트에 보이는 필드
    private String title;

    private int maxPeople;

    private String description;

    private LocalDateTime promiseTime;

    private List<HashTag> hashTag = new ArrayList<HashTag>();

    private List<String> rules = new ArrayList<String>();
}
