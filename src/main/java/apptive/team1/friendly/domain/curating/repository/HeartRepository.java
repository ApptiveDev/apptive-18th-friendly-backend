package apptive.team1.friendly.domain.curating.repository;

import apptive.team1.friendly.domain.curating.entity.Heart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HeartRepository {

    private final EntityManager em;

    public void save(Heart heart) {
        em.persist(heart);
    }

    public void delete(Heart heart) {
        em.remove(heart);
    }

    public List<Heart> findHeartsByContentId(Long contentId) {
        return em.createQuery("select h from Heart h where h.content.id = :contentId", Heart.class)
                .setParameter("contentId", contentId)
                .getResultList();
    }

    public Optional<Heart> findHeartByUserIdAndContentId(Long userId, Long contentId) {
        return Optional.ofNullable(em.createQuery("select h from Heart h where h.account.id = :userId and h.content.id = :contentId", Heart.class)
                .setParameter("userId", userId)
                .setParameter("contentId", contentId)
                .getSingleResult());
    }

}
