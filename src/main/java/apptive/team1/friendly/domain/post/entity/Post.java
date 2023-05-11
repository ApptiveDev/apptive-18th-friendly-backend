package apptive.team1.friendly.domain.post.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;
import java.util.List;

@Entity
@Getter
public class Post {

    private Long id;

    private String title;

    @Column(length = 500)
    private String description;

    private Date promiseTime;

    private String location;

    private int maxPeople;

    private List<String> hashTag;

    private AccountPost accountPost;

    @Column(length = 1000)
    private String rule;

    private String destination;
    // private img


//    public void update(Post post) {
//        title = post.getTitle();
//        description = post.getDescription();
//        promiseTime = post.getPromiseTime();
//        location = post.getLocation();
//        maxPeople = post.getMaxPeople();
//        hashTag = post.hashTag;
//    } 고민...
}
