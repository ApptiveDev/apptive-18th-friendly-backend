package apptive.team1.friendly.domain.post.repository;

import apptive.team1.friendly.domain.post.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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

    public Long delete(Long commentId) {
        Comment comment = em.find(Comment.class, commentId);
        em.remove(comment);
        return commentId;
    }

}
