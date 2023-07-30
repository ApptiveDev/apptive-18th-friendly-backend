package apptive.team1.friendly.domain.curation.repository;

import apptive.team1.friendly.domain.curation.entity.Content;
import apptive.team1.friendly.domain.curation.entity.SearchBase;
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
        return em.createQuery("select c from Content c join fetch c.images join fetch c.account order by c.createdDate", Content.class)
                .getResultList();
    }

    public Content findOne(Long contentId) {
        return em.find(Content.class, contentId);
    }
}
