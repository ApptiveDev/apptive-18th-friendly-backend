package apptive.team1.friendly.domain.user.data.repository;

import apptive.team1.friendly.domain.user.data.entity.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @EntityGraph(attributePaths = "accountAuthorities")
    Optional<Account> findOneWithAccountAuthoritiesByUsername(String username);

}
