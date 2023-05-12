package apptive.team1.friendly.domain.post.repository;

import apptive.team1.friendly.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    public void save(Post post) {
        if(post.getId() == null) {
            em.persist(post); //신규
        }
        else {
            em.merge(post); // 업데이트
        }
    }

    public Post findOneById(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> findAll() {
        return em.createQuery("select p from Post p", Post.class)
                .getResultList();
    }
}
