package apptive.team1.friendly.domain.post.entity;

import apptive.team1.friendly.domain.post.dto.PostFormDto;
import apptive.team1.friendly.global.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseEntity {

    @Builder
    public Post(String title, String description, int maxPeople,
                LocalDateTime promiseTime, String location, Set<String> rules,
                Set<HashTag> hashTag, LocalDateTime createdDate) {
        this.title = title;
        this.description = description;
        this.maxPeople = maxPeople;
        this.promiseTime = promiseTime;
        this.location = location;
        this.rules = rules;
        this.hashTag = hashTag;
        this.setCreatedDate(createdDate);
        this.setLastModifiedDate(createdDate);
    }


    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시판 리스트에 보이는 필드
    private String title;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
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
    @NotNull
    private Set<HashTag> hashTag;

    @ElementCollection(fetch = FetchType.LAZY)
    @NotNull
    private Set<String> rules;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Comment> comments;

    public void update(PostFormDto formDto) {
        this.title = formDto.getTitle();
        this.hashTag = formDto.getHashTag();
        this.maxPeople = formDto.getMaxPeople();
        this.description = formDto.getDescription();
        this.promiseTime = formDto.getPromiseTime();
        this.location = formDto.getLocation();
        this.rules = formDto.getRules();
        this.setLastModifiedDate(LocalDateTime.now());
    }
}
