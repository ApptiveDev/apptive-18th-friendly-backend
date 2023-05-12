package apptive.team1.friendly.domain.post.entity;
import apptive.team1.friendly.domain.user.data.entity.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@Entity
@Getter @Setter
public class AccountPost {

    @Id
    @Column(name = "user_post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터 베이스에 위임 MySQL Auto_Increment
    private Long id;

    @ManyToOne(fetch = LAZY) // 실제로 Post 객체가 필요 할 때 fetch. AccountPost 조회할 때 같이 fetch 하지 않음
    @JoinColumn(name = "post_id") // 연관관계의 주인
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id") // 연관관계의 주인
    private Account user;


    @Override
    public boolean equals(Object o) {
        AccountPost accountPost = (AccountPost) o;
        return this.id == accountPost.id;
    }

    // private img
}
