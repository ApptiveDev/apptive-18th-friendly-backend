package apptive.team1.friendly.domain.curating.repository;

import apptive.team1.friendly.domain.curating.dto.ContentFormDto;
import apptive.team1.friendly.domain.curating.entity.Content;
import apptive.team1.friendly.domain.curating.entity.SearchBase;
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
        if(searchBase == SearchBase.RECOMMENDATION) {
            return em.createQuery("select c from Content c order by c.like", Content.class)
                    .getResultList();
        }
        else {
            return em.createQuery("select c from Content c order by c.createdDate", Content.class)
                    .getResultList();
        }
    }

    public Content findOne(Long contentId) {
        return em.find(Content.class, contentId);
    }
}
