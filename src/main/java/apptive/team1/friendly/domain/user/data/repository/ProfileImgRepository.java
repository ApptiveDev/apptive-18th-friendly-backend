package apptive.team1.friendly.domain.user.data.repository;

import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.entity.profile.ProfileImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileImgRepository extends JpaRepository<ProfileImg, Long> {
    Optional<ProfileImg> findOneByAccount(Account account);
}
