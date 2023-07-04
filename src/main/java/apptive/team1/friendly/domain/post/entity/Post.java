package apptive.team1.friendly.domain.post.entity;

import apptive.team1.friendly.domain.post.dto.PostFormDto;
import apptive.team1.friendly.global.common.s3.FileInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import apptive.team1.friendly.global.common.s3.FileInfo;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Post {

    public Post(String title, String description, int maxPeople, LocalDateTime promiseTime, String location, Set<String> rules, Set<HashTag> hashTag) {
        this.title = title;
        this.description = description;
        this.maxPeople = maxPeople;
        this.promiseTime = promiseTime;
        this.location = location;
        this.rules = rules;
        this.hashTag = hashTag;
    }

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시판 리스트에 보이는 필드
    private String title;

    @OneToMany(mappedBy = "post")
    private List<PostImage> postImages = new ArrayList<>(); // 게시판 이미지

    @Lob
    private String description;

    private int maxPeople;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime promiseTime;

    private String location;


    // 게시글에 들어가야 보이는 필드

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private Set<HashTag> hashTag;

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<String> rules;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Post 저장시 연관되어 있는 Comment들도 함께 저장
    private Set<Comment> comments;


    public void update(PostFormDto formDto) {
        this.title = formDto.getTitle();
        this.hashTag = formDto.getHashTag();
        this.maxPeople = formDto.getMaxPeople();
        this.description = formDto.getDescription();
        this.promiseTime = formDto.getPromiseTime();
        this.location = formDto.getLocation();
        this.rules = formDto.getRules();
    }
}
