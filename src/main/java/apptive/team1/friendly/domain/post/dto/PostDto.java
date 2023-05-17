package apptive.team1.friendly.domain.post.dto;

import apptive.team1.friendly.domain.post.entity.AccountPost;
import apptive.team1.friendly.domain.post.entity.HashTag;
import apptive.team1.friendly.domain.user.data.entity.Account;
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

    private String title;

    private List<HashTag> hashTag = new ArrayList<HashTag>();

    private int maxPeople;

    private Long authorId;

    private String description;

    private LocalDateTime promiseTime;

    private String location;

    private List<String> rules = new ArrayList<String>();
}
