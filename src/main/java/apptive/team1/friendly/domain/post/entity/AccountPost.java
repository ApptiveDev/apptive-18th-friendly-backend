package apptive.team1.friendly.domain.post.entity;
import apptive.team1.friendly.domain.user.data.entity.Account;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountPost {

    @Id
    @Column(name = "user_post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터 베이스에 위임 MySQL Auto_Increment
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id") // 연관관계의 주인
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 연관관계의 주인
    private Account user;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Builder
    public AccountPost(Post post, Account user, AccountType accountType) {
        this.post = post;
        this.user = user;
        this.accountType = accountType;
    }
    public static AccountPost createAccountPost(Account account, Post post, AccountType accountType) {
        return AccountPost.builder()
                .user(account)
                .post(post)
                .accountType(accountType)
                .build();
    }

}
