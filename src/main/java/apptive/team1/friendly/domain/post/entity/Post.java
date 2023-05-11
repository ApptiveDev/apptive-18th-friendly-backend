package apptive.team1.friendly.domain.post.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
public class Post {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 500)
    private String description;

    private Date promiseTime;

    private String location;

    private int maxPeople;

//    private List<String> hashTag = new ArrayList<String>();

    @Column(length = 1000)
    private String rule;

    private String destination;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<AccountPost> accountPosts = new ArrayList<AccountPost>();

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
