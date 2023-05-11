package apptive.team1.friendly.domain.post.entity;
import apptive.team1.friendly.domain.user.data.entity.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@Entity
@Getter @Setter
public class AccountPost {

    @Id
    @Column(name = "user_post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private Account user;
}
