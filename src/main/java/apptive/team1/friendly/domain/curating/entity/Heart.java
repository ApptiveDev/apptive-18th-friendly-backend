package apptive.team1.friendly.domain.curating.entity;

import apptive.team1.friendly.domain.user.data.entity.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Heart {
    public Heart(Account account, Content content) {
        this.account = account;
        this.content = content;
    }

    @Id @GeneratedValue
    @Column(name = "heart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="content_id")
    private Content content;

    public static Heart create(Account currentUser, Content content) {
        Heart heart = new Heart(currentUser, content);
        content.getHearts().add(heart);
        return heart;
    }
}
