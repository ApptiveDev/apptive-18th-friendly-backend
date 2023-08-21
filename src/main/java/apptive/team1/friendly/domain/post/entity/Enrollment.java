package apptive.team1.friendly.domain.post.entity;

import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.global.baseEntity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Getter
@NoArgsConstructor
public class Enrollment extends BaseEntity {

    @Builder
    public Enrollment(Post post, Account account, boolean isAccepted) {
        this.post = post;
        this.account = account;
        this.isAccepted = isAccepted;
    }

    @Id @GeneratedValue
    @Column(name = "enrollment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Account account;

    private LocalDateTime enrolledAt;

    private boolean isAccepted;


    //======== 정적 생성 메서드 =========//
    public static Enrollment create(Account account, Post post) {
        return Enrollment.builder()
                .account(account)
                .post(post)
                .isAccepted(false)
                .build();
    }

    //====== 비즈니스 로직 ======//

    public void accept() {
        this.isAccepted = true;
        this.enrolledAt = LocalDateTime.now();
    }

    public void reject() {
        this.isAccepted = false;
    }
}
