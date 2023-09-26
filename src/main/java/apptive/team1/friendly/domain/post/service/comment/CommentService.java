package apptive.team1.friendly.domain.post.service.comment;

import apptive.team1.friendly.domain.post.dto.comment.CommentFormDto;
import apptive.team1.friendly.domain.post.entity.comment.Comment;
import apptive.team1.friendly.domain.post.exception.AccessDeniedException;
import apptive.team1.friendly.domain.post.exception.NotFoundCommentException;
import apptive.team1.friendly.domain.post.repository.comment.CommentRepository;
import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.post.repository.PostRepository;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
import apptive.team1.friendly.global.error.ErrorCode;
import apptive.team1.friendly.global.error.exception.CustomException;
import apptive.team1.friendly.global.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

import static apptive.team1.friendly.domain.post.service.PostServiceHelper.findExistingPost;

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
        Account author = SecurityUtil.getCurrentUserName().flatMap(accountRepository::findOneWithAccountAuthoritiesByEmail).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 댓글을 작성하는 게시물 확인
        Post post = findExistingPost(postRepository, postId);

        // Comment 객체 생성하고 데이터 이동
        Comment newComment = Comment.builder()
                .text(commentFormDto.getText())
                .account(author)
                .post(post)
                .createTime(LocalDateTime.now())
                .build();

        // 저장하면 id 생김
        commentRepository.save(newComment);

        // id 리턴
        return newComment.getId();
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundCommentException("댓글을 찾을 수 없습니다."));
        checkCommentAuthor(comment, userId);
        commentRepository.delete(comment);
    }

    private void checkCommentAuthor(Comment comment, Long userId) {
        if (!Objects.equals(comment.getAccount().getId(), userId)) {
            throw new AccessDeniedException("댓글 삭제 권한이 없습니다.");
        }
    }
}
