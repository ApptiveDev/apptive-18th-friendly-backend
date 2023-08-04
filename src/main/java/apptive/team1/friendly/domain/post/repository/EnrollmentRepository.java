package apptive.team1.friendly.domain.post.repository;

import apptive.team1.friendly.domain.post.entity.Enrollment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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
}
