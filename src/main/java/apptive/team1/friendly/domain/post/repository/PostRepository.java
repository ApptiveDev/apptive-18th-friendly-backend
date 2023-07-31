package apptive.team1.friendly.domain.post.repository;

import apptive.team1.friendly.domain.post.entity.HashTag;
import apptive.team1.friendly.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    /**
     * 임의의 user가 쓴 게시믈 userId로 조회
     */
    public List<Post> findByUser(Long userId) {
        List<Post> posts = em.createQuery("select distinct p from Post p join AccountPost ap on ap.user.id = :userId where ap.post.id = p.id", Post.class)
                .setParameter("userId", userId)
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
    public Post findOneByPostId(Long postId) {
        return em.createQuery("select distinct p from Post p join fetch p.hashTags where p.id =: postId", Post.class)
                .setParameter("postId", postId)
                .getSingleResult();
    }

    /**
     * 전체 게시물 조회
     */
    public List<Post> findAll(String tag, String keyword) {
        if(tag == null && keyword == null)
            return em.createQuery("select distinct p from Post p join fetch p.hashTags", Post.class)
                    .getResultList();
        else if(tag == null) {
            return em.createQuery("select distinct p from Post p join fetch p.hashTags where p.title like :keyword or p.description like :keyword or p.location like :keyword", Post.class)
                    .setParameter("keyword", "%"+keyword+"%")
                    .getResultList();
        }
        else if(keyword == null) {
            return em.createQuery("select distinct p from Post p join fetch p.hashTags where :tag MEMBER OF p.hashTags", Post.class)
                    .setParameter("tag", HashTag.valueOf(tag.toUpperCase()))
                    .getResultList();
        }
        else {
            return em.createQuery("select distinct p from Post p join fetch p.hashTags where :tag MEMBER OF p.hashTags and p.title like :keyword or p.description like :keyword or p.location like :keyword", Post.class)
                    .setParameter("tag", HashTag.valueOf(tag.toUpperCase()))
                    .setParameter("keyword", "%"+keyword+"%")
                    .getResultList();
        }
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
