package apptive.team1.friendly.domain.user.data.repository;

import apptive.team1.friendly.domain.user.data.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
