package apptive.team1.friendly.domain.user.data.repository;

import apptive.team1.friendly.domain.user.data.entity.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @EntityGraph(attributePaths = "accountAuthorities")
    Optional<Account> findOneWithAccountAuthoritiesByEmail(String email);

    @EntityGraph(attributePaths = "accountAuthorities")
    Optional<Account> findOneWithAccountAuthoritiesById(Long id);

    Optional<Account> findOneByEmail(String email);

    @Query("select distinct a from Account a left join fetch a.languages join AccountPost ap on ap.accountType = apptive.team1.friendly.domain.post.entity.AccountType.AUTHOR and ap.post.id = :postId where a.id = ap.user.id")
    Account findAuthorByPostId(@Param("postId") Long postId);

    @Query("select distinct a from Account a left join fetch a.languages join AccountPost ap on ap.accountType = apptive.team1.friendly.domain.post.entity.AccountType.PARTICIPANT and ap.post.id = :postId")
    List<Account> getAccountsByPostId(@Param("postId") Long postId);
}