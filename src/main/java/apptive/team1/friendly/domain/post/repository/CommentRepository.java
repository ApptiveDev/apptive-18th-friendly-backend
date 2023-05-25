package apptive.team1.friendly.domain.post.repository;

import apptive.team1.friendly.domain.post.entity.Comment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class CommentRepository {

    EntityManager em;

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

    public Long delete(Long commentId) {
        Comment comment = em.find(Comment.class, commentId);
        em.remove(comment);
        return commentId;
    }

}
