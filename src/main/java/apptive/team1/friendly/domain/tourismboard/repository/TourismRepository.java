package apptive.team1.friendly.domain.tourismboard.repository;

import apptive.team1.friendly.domain.tourismboard.entity.FamousRestaurant;
import apptive.team1.friendly.domain.tourismboard.entity.ThemeTourism;
import apptive.team1.friendly.domain.tourismboard.entity.Tourism;
import apptive.team1.friendly.domain.tourismboard.entity.WalkingTourism;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TourismRepository {
    private final EntityManager em;
    private static final int resultCount = 10;
    public void save(Tourism tourism) {
        em.persist(tourism);
    }

    public Tourism findOneById(Long tourismId) {
        return em.find(Tourism.class, tourismId);
    }

    public int deleteAll() {
        return em.createQuery("delete from Tourism")
                .executeUpdate();
    }

    public List<Tourism> getTourismList(int pageNum, String tag) { // 동적 쿼리로 수정
        TypedQuery<Tourism> query;
        if(tag == null)
            query = em.createQuery("select t from Tourism t ORDER BY RAND()", Tourism.class);
        else if(tag.equals("walking"))
            query = em.createQuery("select wt from WalkingTourism wt", Tourism.class);
        else if(tag.equals("theme"))
            query = em.createQuery("select tt from ThemeTourism tt", Tourism.class);
        else if(tag.equals("restaurant"))
            query = em.createQuery("select fr from FamousRestaurant fr", Tourism.class);
        else
            return Collections.emptyList();

        return query
                .setFirstResult(pageNum * resultCount)
                .setMaxResults(resultCount)
                .getResultList();
    }
}
