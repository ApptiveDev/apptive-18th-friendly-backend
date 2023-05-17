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
     * 게시물 저장
     */
    public void save(Post post) {
        if(post.getId() == null) {
            em.persist(post); //신규
        }
        else {
            em.merge(post); // 업데이트
        }
    }

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

    /**
     * 임의의 user가 쓴 게시믈 조회
     */
    public List<Post> findByUser(Long userId) {
        Account user = em.find(Account.class, userId);
        List<AccountPost> accountPosts = user.getAccountPosts();

        List<Post> posts = new ArrayList<Post>();
        for(AccountPost accountPost: accountPosts) {
            Post post = accountPost.getPost();
            posts.add(post);
        }
        return posts;
    }


}
