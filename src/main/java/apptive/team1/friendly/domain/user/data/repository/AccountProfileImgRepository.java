package apptive.team1.friendly.domain.user.data.repository;

import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.entity.profile.ProfileImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AccountProfileImgRepository extends JpaRepository<ProfileImg, Long> {
    Optional<ProfileImg> findOneByAccount(Account account);

    @Transactional
    void deleteByAccount(Account account);
}
