package apptive.team1.friendly.domain.post.entity;
import apptive.team1.friendly.domain.user.data.entity.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.Optional;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@Entity
@Getter @Setter
public class AccountPost {

    @Id
    @Column(name = "user_post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터 베이스에 위임 MySQL Auto_Increment
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = LAZY) // 실제로 Post 객체가 필요 할 때 fetch. AccountPost 조회할 때 같이 fetch 하지 않음
    @JoinColumn(name = "post_id") // 연관관계의 주인
    private Post post;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = LAZY) // AccountPost객체가 제거될 때 user도 같이 제거되면 안되므로 저장할 때만 반영
    @JoinColumn(name = "user_id") // 연관관계의 주인
    private Account user;


    @Override
    public boolean equals(Object o) {
        AccountPost accountPost = (AccountPost) o;
        return this.id == accountPost.id;
    }


}
