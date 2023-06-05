package apptive.team1.friendly.domain.post.entity;

import apptive.team1.friendly.domain.user.data.entity.Account;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Comment {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String text;

    @JoinColumn(name = "user_id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Account account;

    @JoinColumn(name = "post_id")
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Post post;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 연관관계 편의 메소드
     */
    public void setPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }

}
