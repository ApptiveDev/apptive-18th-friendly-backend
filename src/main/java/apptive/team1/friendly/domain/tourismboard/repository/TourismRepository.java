package apptive.team1.friendly.domain.tourismboard.repository;

import apptive.team1.friendly.domain.tourismboard.entity.FamousRestaurant;
import apptive.team1.friendly.domain.tourismboard.entity.ThemeTourism;
import apptive.team1.friendly.domain.tourismboard.entity.Tourism;
import apptive.team1.friendly.domain.tourismboard.entity.WalkingTourism;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TourismRepository {
    private final EntityManager em;
    private static final int resultCount = 10;
    public void save(Tourism tourism) {
        if(tourism.getId() != null) {
            em.persist(tourism);
        }
        else {
            em.merge(tourism);
        }
    }

    public List<Tourism> getAllTourism(int pageNum) {
        return em.createQuery("select t from Tourism t", Tourism.class)
                .setFirstResult(pageNum*resultCount)
                .setMaxResults(resultCount)
                .getResultList();
    }

    public List<WalkingTourism> getWalkingTourism(int pageNum) {
        return em.createQuery("select wt from WalkingTourism wt", WalkingTourism.class)
                .setFirstResult(pageNum*resultCount)
                .setMaxResults(resultCount)
                .getResultList();
    }

    public List<ThemeTourism> getThemeTourism(int pageNum) {
        return em.createQuery("select tt from ThemeTourism tt", ThemeTourism.class)
                .setFirstResult(pageNum*resultCount)
                .setMaxResults(resultCount)
                .getResultList();
    }

    public List<FamousRestaurant> getFamousRestaurant(int pageNum) {
        return em.createQuery("select fr from FamousRestaurant fr", FamousRestaurant.class)
                .setFirstResult(pageNum*resultCount)
                .setMaxResults(resultCount)
                .getResultList();
    }

    public int deleteAll() {
        return em.createQuery("delete from Tourism")
                .executeUpdate();
    }
}
