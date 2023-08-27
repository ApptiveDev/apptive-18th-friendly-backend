package apptive.team1.friendly.domain.audioguide.repository;

import apptive.team1.friendly.domain.post.entity.AudioGuide;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AudioGuideRepository {

    private final EntityManager em;

    public List<AudioGuide> findAudioGuides(int pageNum, String keyword) {
        int resultCount = 10;
        if(keyword == null) {
            return em.createQuery("select ag from AudioGuide ag", AudioGuide.class)
                    .setFirstResult(pageNum * resultCount)
                    .setMaxResults(resultCount)
                    .getResultList();
        }

        return em.createQuery("select ag from AudioGuide ag where ag.title like :keyword or ag.audioTitle like :keyword or ag.script like :keyword", AudioGuide.class)
                .setParameter("keyword", "%"+keyword+"%")
                .setFirstResult(pageNum * resultCount)
                .setMaxResults(resultCount)
                .getResultList();
    }

    @Transactional
    public void save(AudioGuide audioGuide) {
        em.persist(audioGuide);
    }
}
