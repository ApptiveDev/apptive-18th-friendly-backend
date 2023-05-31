package apptive.team1.friendly.domain.user.data.entity.profile;

import apptive.team1.friendly.domain.user.data.constant.LanguageLevel;
import apptive.team1.friendly.domain.user.data.entity.Account;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountLanguage {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @Enumerated(EnumType.STRING)
    private LanguageLevel languageLevel;
}
