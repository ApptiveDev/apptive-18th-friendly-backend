package apptive.team1.friendly.domain.post.repository;

import apptive.team1.friendly.domain.post.entity.Enrollment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EnrollmentRepository {

    private final EntityManager em;

    public void save(Enrollment enrollment) {
        em.persist(enrollment);
    }

    public void delete(Enrollment enrollment) {
        em.remove(enrollment);
    }

    public Enrollment findOneById(Long enrollmentId) {
        return em.find(Enrollment.class, enrollmentId);
    }

    public Enrollment findOneByAccountAndPost(Long accountId, Long postId) {
        List<Enrollment> enrollments = em.createQuery("select e from Enrollment e where e.account.id = :accountId and e.post.id = :postId", Enrollment.class)
                .setParameter("accountId", accountId)
                .setParameter("postId", postId)
                .getResultList();

        if(enrollments.isEmpty())
            return null;
        else
            return enrollments.get(0);
    }

    public List<Enrollment> findByPost(Long postId) {
        return em.createQuery("select e from Enrollment e where e.post.id = :postId", Enrollment.class)
                .setParameter("postId", postId)
                .getResultList();
    }
}
