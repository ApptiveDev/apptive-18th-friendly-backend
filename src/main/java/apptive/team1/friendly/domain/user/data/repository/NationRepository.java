package apptive.team1.friendly.domain.user.data.repository;

import apptive.team1.friendly.domain.user.data.entity.profile.Nation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NationRepository extends JpaRepository<Nation, Long> {
    Optional<Nation> findOneByName(String name);
}
