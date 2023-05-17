package apptive.team1.friendly.domain.user.data.repository;

import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.entity.profile.AccountLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountLanguageRepository extends JpaRepository<AccountLanguage, Long> {
    List<AccountLanguage> findAllByAccount(Account account);

    @Transactional
    void deleteByAccount(Account account);
}
