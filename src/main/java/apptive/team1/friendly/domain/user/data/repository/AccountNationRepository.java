package apptive.team1.friendly.domain.user.data.repository;

import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.entity.profile.AccountNation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountNationRepository extends JpaRepository<AccountNation, Long> {
    Optional<AccountNation> findOneByAccount(Account account);
}
