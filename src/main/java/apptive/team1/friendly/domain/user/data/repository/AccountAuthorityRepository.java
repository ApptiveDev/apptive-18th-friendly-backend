package apptive.team1.friendly.domain.user.data.repository;

import apptive.team1.friendly.domain.user.data.entity.AccountAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountAuthorityRepository extends JpaRepository<AccountAuthority, Long> {
}
