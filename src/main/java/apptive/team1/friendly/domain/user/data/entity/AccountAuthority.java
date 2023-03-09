package apptive.team1.friendly.domain.user.data.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountAuthority {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "authority_id")
    private Authority authority;
}
