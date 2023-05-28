package apptive.team1.friendly.domain.post.entity;

import apptive.team1.friendly.global.common.s3.FileInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Post {

    public Post(String title, String description, int maxPeople, LocalDateTime promiseTime, String location, List<String> rules, List<HashTag> hashTag) {
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

    @Column(length = 500)
    private String description;

    private int maxPeople;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime promiseTime;

    private String location;


    // 게시글에 들어가야 보이는 필드

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<HashTag> hashTag = new ArrayList<HashTag>();

    @ElementCollection
    private List<String> rules = new ArrayList<String>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL) // Post 저장시 연관되어 있는 Comment들도 함께 저장
    private List<Comment> comments = new ArrayList<Comment>();
}
