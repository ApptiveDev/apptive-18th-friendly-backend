package apptive.team1.friendly.domain.curation.repository;

import apptive.team1.friendly.domain.curation.entity.Content;
import apptive.team1.friendly.domain.curation.entity.SearchBase;
import apptive.team1.friendly.domain.user.data.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ContentRepository {
    private final EntityManager em;
    public void save(Content content) {
        em.persist(content);
    }

    public void delete(Content content) {
        em.remove(content);
    }

    public List<Content> findAll(SearchBase searchBase) {
        if(searchBase == SearchBase.LATEST)
            return em.createQuery("select distinct c from Content c join fetch c.images order by c.createdDate desc", Content.class)
                    .getResultList();
        else {
            return em.createQuery("select distinct c from Content c join fetch c.images order by c.hearts.size desc", Content.class)
                    .getResultList();
        }
    }

    public Content findOne(Long contentId) {
        return em.createQuery("select distinct c from Content c join fetch c.account left join fetch c.images where c.id = :contentId", Content.class)
                .setParameter("contentId", contentId)
                .getSingleResult();
    }
}
