package apptive.team1.friendly.domain.user.data.repository;

import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.entity.profile.AccountInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountInterestRepository extends JpaRepository<AccountInterest, Long> {
    List<AccountInterest> findAllByAccount(Account account);

    @Transactional
    void deleteByAccount(Account account);
}
