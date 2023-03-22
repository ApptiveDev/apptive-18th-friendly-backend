package apptive.team1.friendly.domain.user.data.repository;

import apptive.team1.friendly.domain.user.data.entity.profile.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findOneByName(String name);
}
