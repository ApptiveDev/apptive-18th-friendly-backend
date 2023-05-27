package apptive.team1.friendly.domain.post.repository;

import apptive.team1.friendly.domain.post.entity.AccountPost;
import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.user.data.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    /**
     * 임의의 user가 쓴 게시믈 userId로 조회
     */
    public List<Post> findByUser(Long userId) {
        // userId에 해당하는 AccountPost 객체 리스트를 찾는다
        List<AccountPost> accountPosts = em.createQuery("select ap from AccountPost ap where ap.user.id =: userId", AccountPost.class)
                .setParameter("userId", userId)
                .getResultList();

        List<Post> posts = new ArrayList<>();

        // AccountPost 리스트를 순회하며 연관된 post를 추가한다
        for(AccountPost accountPost : accountPosts) {
            posts.add(accountPost.getPost());
        }

        return posts;
    }

    /**
     * 게시물 저장
     * AccountPost에서 Cascade.ALL 사용으로 post 자체의 저장, 삭제기능 필요없음
     */
//    public void save(Post post) {
//        if(post.getId() == null) {
//            em.persist(post); //신규
//        }
//        else {
//            em.merge(post); // 업데이트
//        }
//    }

    /**
     * 게시물 찾기
     */
    public Post findOneByPostId(Long postId) {
        return em.find(Post.class, postId);
    }

    /**
     * 전체 게시물 조회
     */
    public List<Post> findAll() {
        return em.createQuery("select p from Post p", Post.class)
                .getResultList();
    }


}
