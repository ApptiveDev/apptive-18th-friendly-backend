package apptive.team1.friendly.domain.post.repository;

import apptive.team1.friendly.domain.post.dto.PostFormDto;
import apptive.team1.friendly.domain.post.entity.AccountPost;
import apptive.team1.friendly.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccountPostRepository {
    private final EntityManager em;

    /**
     * accountPost 저장
     */
    public Long save(AccountPost accountPost) {
        if(accountPost.getId() == null) {
            em.persist(accountPost); //신규
            return accountPost.getId();
        }
        else {
            throw new RuntimeException("이미 존재하는 글입니다.");
        }
    }


    /**
     * 임의의 user가 쓴 게시믈 userId로 조회
     */
    public List<Post> findByUser(Long userId) {
        List<AccountPost> accountPosts = em.createQuery("select ap from AccountPost ap where ap.user.id =: userId", AccountPost.class)
                .setParameter("userId", userId)
                .getResultList();

        List<Post> posts = new ArrayList<>();

        for(AccountPost accountPost : accountPosts) {
            posts.add(accountPost.getPost());
        }

        return posts;
    }

    /**
     * 게시물 삭제
     */
    public Long delete(Long userId, Long postId) {
        // postId에 해당하는 AccountPost 찾음
        AccountPost findAccountPost = em.createQuery("select ap from AccountPost ap where ap.post.id = :postId", AccountPost.class)
                .setParameter("postId", postId)
                .getSingleResult();

        // AccountPost에서 user를 찾아서 userId와 비교
        if(findAccountPost.getUser().getId() != userId) // 본인 게시물이 아니면 삭제 불가
            throw new RuntimeException("접근 권한이 없습니다.");

        // AccountPost 삭제, CascadeType.remove 이기 때문에 Post도 함께 삭제 됨
        em.remove(findAccountPost);

        return postId;
    }

    /**
     * 게시물 업데이트
     */
    public Long update(Long postId, Long userId, PostFormDto updatePostDto) {
        // postId에 해당하는 AccountPost 찾음
        AccountPost findAccountPost = em.createQuery("select ap from AccountPost ap where ap.post.id = :postId", AccountPost.class)
                .setParameter("postId", postId)
                .getSingleResult();

        // 현재 로그인된 user와 게시글의 user가 다르면 예외 처리
        if(userId != findAccountPost.getUser().getId())
            throw new RuntimeException("권한이 없습니다."); // 본인 게시물 아니면 수정 불가

        // AccountPost에서 post 찾기
        Post findPost = findAccountPost.getPost();

        // 찾은 게시물에 값 옮기기
        findPost.setTitle(updatePostDto.getTitle());
        findPost.setHashTag(updatePostDto.getHashTag());
        findPost.setLocation(updatePostDto.getLocation());
        findPost.setRules(updatePostDto.getRules());
        findPost.setMaxPeople(updatePostDto.getMaxPeople());
        findPost.setDescription(updatePostDto.getDescription());
        findPost.setPromiseTime(updatePostDto.getPromiseTime());

        return findPost.getId();
    }
}
