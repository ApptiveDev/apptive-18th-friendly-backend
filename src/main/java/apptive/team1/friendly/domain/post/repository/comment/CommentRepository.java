package apptive.team1.friendly.domain.post.repository.comment;

import apptive.team1.friendly.domain.post.entity.comment.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    /**
     * 댓글 작성, 수정
     */
    public void save(Comment newComment) {
        if(newComment.getId() == null)
            em.persist(newComment);
        else{
            em.merge(newComment);
        }
    }

    public void delete(Comment comment) {
        em.remove(comment);
    }

    public Long deleteById(Long commentId) {
        Comment comment = em.find(Comment.class, commentId);
        em.remove(comment);
        return commentId;
    }

    public Optional<Comment> findById(Long commentId) {
        try {
            Comment comment = em.createQuery("select distinct c from Comment c left join fetch c.account where c.id =: commentId", Comment.class)
                    .setParameter("commentId", commentId)
                    .getSingleResult();
            return Optional.ofNullable(comment);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (NonUniqueResultException e) {
            throw new IllegalStateException("둘 이상의 Comment가 동일한 ID로 조회되었습니다: " + commentId);
        }
    }
}
