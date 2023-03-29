package apptive.team1.friendly.domain.user.data.repository;

import apptive.team1.friendly.domain.user.data.entity.profile.LanguageLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageLevelRepository extends JpaRepository<LanguageLevel ,Long> {
    Optional<LanguageLevel> findOneByName(String name);
}
