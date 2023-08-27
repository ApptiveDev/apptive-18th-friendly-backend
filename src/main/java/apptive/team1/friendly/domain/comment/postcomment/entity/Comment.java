package apptive.team1.friendly.domain.comment.postcomment.entity;

import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.global.baseEntity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Builder
    public Comment(String text, Account account, Post post, LocalDateTime createTime) {
        this.text = text;
        this.account = account;
        this.post = post;
        this.setCreatedDate(createTime);
        this.setLastModifiedDate(createTime);
    }

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String text;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    /**
     * 연관관계 편의 메소드
     */
    public void setPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }

}
