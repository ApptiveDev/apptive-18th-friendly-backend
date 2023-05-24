package apptive.team1.friendly.domain.post.entity;

import apptive.team1.friendly.domain.post.dto.UpdatePostDto;
import apptive.team1.friendly.domain.user.data.entity.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.invoke.MethodHandles.throwException;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Post {

    public Post(String title, String description, int maxPeople, LocalDateTime promiseTime, String location, List<String> rules, List<HashTag> hashTag) {
//        accountPosts.add(accountPost);
//        accountPost.setPost(this);
//        this.author = accountPost;
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

    private String image;

    // 게시글에 들어가야 보이는 필드

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<HashTag> hashTag = new ArrayList<HashTag>();

    @ElementCollection
    private List<String> rules = new ArrayList<String>();


}
