package apptive.team1.friendly.domain.post.service;

import apptive.team1.friendly.domain.post.dto.CommentFormDto;
import apptive.team1.friendly.domain.post.entity.Comment;
import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.post.repository.CommentRepository;
import apptive.team1.friendly.domain.post.repository.PostRepository;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Long addComment(CommentFormDto commentFormDto, Long postId) {
        // 현재 로그인된 사용자 확인
//        Account author = SecurityUtil.getCurrentUserName().flatMap(accountRepository::findOneWithAccountAuthoritiesByEmail).orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        // test용
        Account author = new Account();
        author.setEmail("test@test");
        author.setFirstName("mw");
        author.setLastName("mw2");
        //

        // 댓글을 작성하는 게시물 확인
        Post post = postRepository.findOneByPostId(postId);

        // Comment 객체 생성하고 데이터 이동
        Comment newComment = new Comment();
        newComment.setText(commentFormDto.getText());
        newComment.setCreateTime(LocalDateTime.now());
        
        // 연관관계 설정
        newComment.setAccount(author);
        newComment.setPost(post);

        // 저장하면 id 생김
        commentRepository.save(newComment);

        // id 리턴
        return newComment.getId();
    }
}
