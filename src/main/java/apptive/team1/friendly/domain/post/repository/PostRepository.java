package apptive.team1.friendly.domain.post.repository;

import apptive.team1.friendly.domain.post.entity.AccountType;
import apptive.team1.friendly.domain.post.entity.HashTag;
import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.user.data.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    /**
     * 임의의 user가 쓴 게시믈 userId로 조회
     */
    public List<Post> findByUser(Long userId) {
        return em.createQuery("select distinct p from Post p left join fetch p.hashTags join AccountPost ap on ap.user.id = :userId where ap.post.id = p.id order by p.lastModifiedDate desc", Post.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    /**
     * user가 작성한 게시물 조회
     */
    public List<Post> findByAuthor(Long userId) {
        return em.createQuery("select distinct p from Post p left join fetch p.hashTags join AccountPost ap on ap.user.id = :userId and ap.accountType = :accountType where ap.post.id = p.id order by p.lastModifiedDate desc", Post.class)
                .setParameter("userId", userId)
                .setParameter("accountType", AccountType.AUTHOR)
                .getResultList();
    }

    /**
     * user가 참가한 게시물 조회
     */
    public List<Post> findByParticipant(Long userId) {
        List<Post> posts = em.createQuery("select distinct p from Post p left join fetch p.hashTags join AccountPost ap on ap.user.id = :userId and ap.accountType = :accountType where ap.post.id = p.id order by p.lastModifiedDate desc", Post.class)
                .setParameter("userId", userId)
                .setParameter("accountType", AccountType.PARTICIPANT)
                .getResultList();

        return posts;
    }


    /**
     * 게시물 저장
     */
    public void save(Post post) {
        em.persist(post); //신규
    }

    /**
     * 게시물 삭제
     */
    public Long delete(Post post) {
        em.remove(post);
        return post.getId();
    }

    /**
     * 게시물 찾기
     */
    public Optional<Post> findOneByPostId(Long postId) {
        List<Post> posts = em.createQuery("select distinct p from Post p left join fetch p.hashTags where p.id =: postId", Post.class)
                .setParameter("postId", postId)
                .getResultList();
        if(posts.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(posts.get(0));
    }

    /**
     * 전체 게시물 조회
     */
    public List<Post> findAll(String tag, String keyword) {
        if(tag == null && keyword == null)
            return em.createQuery("select distinct p from Post p left join fetch p.hashTags", Post.class)
                    .getResultList();
        else if(tag == null) {
            return em.createQuery("select distinct p from Post p left join fetch p.hashTags where p.title like :keyword or p.description like :keyword or p.location like :keyword", Post.class)
                    .setParameter("keyword", "%"+keyword+"%")
                    .getResultList();
        }
        else if(keyword == null) {
            return em.createQuery("select distinct p from Post p left join fetch p.hashTags where :tag MEMBER OF p.hashTags", Post.class)
                    .setParameter("tag", HashTag.valueOf(tag.toUpperCase()))
                    .getResultList();
        }
        else {
            return em.createQuery("select distinct p from Post p left join fetch p.hashTags where :tag MEMBER OF p.hashTags and (p.title like :keyword or p.description like :keyword or p.location like :keyword)", Post.class)
                    .setParameter("tag", HashTag.valueOf(tag.toUpperCase()))
                    .setParameter("keyword", "%"+keyword+"%")
                    .getResultList();
        }
    }

    public void deleteAllByUser(Account account) {
        em.createQuery("delete AccountPost ap where ap.user = :account")
                .setParameter("account", account)
                .executeUpdate();
    }

//    /**
//     * 게시물 전체 이미지 삭제
//     */
//    public int deleteAllImages(Post post) {
//        int deletedImageNum = em.createQuery("delete PostImage pimg where pimg.post = :post")
//                .setParameter("post", post)
//                .executeUpdate();
//        em.clear();
//        return deletedImageNum;
//    }
}
