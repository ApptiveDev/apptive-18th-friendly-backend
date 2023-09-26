package apptive.team1.friendly.domain.post.controller.comment;

import apptive.team1.friendly.domain.post.dto.comment.CommentFormDto;
import apptive.team1.friendly.domain.post.service.comment.CommentService;
import apptive.team1.friendly.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final UserService userService;
    private final CommentService commentService;
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Long> addComment(@PathVariable(value = "postId") Long postId, @RequestBody CommentFormDto comment) {
        Long commentId = commentService.addComment(comment, postId);
        return new ResponseEntity<>(commentId, HttpStatus.OK);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable(value = "commentId") Long commentId) {
        Long currentUserId = userService.getCurrentUser().getId();

        commentService.deleteComment(commentId, currentUserId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
