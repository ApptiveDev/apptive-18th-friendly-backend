package apptive.team1.friendly.domain.user.data.entity.profile;

import apptive.team1.friendly.domain.user.data.entity.Account;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInterest {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "interest_id")
    private Interest interest;
}
