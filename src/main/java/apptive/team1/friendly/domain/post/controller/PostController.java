package apptive.team1.friendly.domain.post.controller;

import apptive.team1.friendly.domain.post.dto.PostDto;
import apptive.team1.friendly.domain.post.dto.PostFormDto;
import apptive.team1.friendly.domain.post.dto.PostListDto;
import apptive.team1.friendly.domain.post.service.MyPageService;
import apptive.team1.friendly.domain.post.service.PostCRUDService;
import apptive.team1.friendly.domain.post.service.PostJoinService;
import apptive.team1.friendly.domain.user.data.dto.UserInfo;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final UserService userService;

    private final PostCRUDService postCRUDService;

    private final MyPageService myPageService;

    private final PostJoinService postJoinService;

    /**
     * 게시물 추가
     */
    @GetMapping("/posts/create") // 게시물 추가 화면 구성
    public ResponseEntity<UserInfo> addPost() {
        UserInfo userInfo = userService.getCurrentUserInfo();
        return ResponseEntity.status(HttpStatus.OK).body(userInfo);
    }

    @PostMapping("/posts/create") // 게시물 추가
    public ResponseEntity<Long> addPost(@RequestPart PostFormDto postForm, @RequestPart List<MultipartFile> imageFiles) throws IOException {
        Long authorId = userService.getCurrentUser().getId();
        Long postId = postCRUDService.addPost(authorId, postForm, imageFiles);
        return ResponseEntity.status(HttpStatus.OK).body(postId);
    }

    /**
     * 게시물 삭제
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Long> deletePost(@PathVariable("postId") Long postId) {
        Long currentUserId = userService.getCurrentUser().getId();
        // 게시물 삭제
        Long deletedPostId = postCRUDService.deletePost(currentUserId, postId);

        // 삭제된 게시물 개수 반환
        return ResponseEntity.status(HttpStatus.OK).body(deletedPostId);
    }

    /**
     * 게시물 수정
     */
    @GetMapping("/posts/{postId}/edit") // 업데이트 페이지 화면 구성
    public ResponseEntity<PostFormDto> updatePostForm(@PathVariable("postId") Long postId) {
        PostFormDto updateForm = postCRUDService.getUpdateForm(postId);
        return ResponseEntity.status(HttpStatus.OK).body(updateForm);
    }

    @PutMapping("/posts/{postId}/edit") // 게시물 업데이트
    public ResponseEntity<Long> updatePost(@PathVariable("postId") Long postId, @RequestPart PostFormDto postForm, @RequestPart List<MultipartFile> imageFiles) throws IOException {
        Long currentUserId = userService.getCurrentUser().getId();
        Long updatedPostId = postCRUDService.updatePost(currentUserId, postId, postForm, imageFiles);
        return new ResponseEntity<>(updatedPostId, HttpStatus.OK);
    }

    /**
     * 게시믈 리스트 검색
     */
    @GetMapping("/posts" )
    public ResponseEntity<List<PostListDto>> postList(@RequestParam(required = false) String tag, @RequestParam(required = false) String keyword) {
        List<PostListDto> postListDtos = postCRUDService.findAll(tag, keyword);
        return new ResponseEntity<>(postListDtos, HttpStatus.OK);
    }

    /**
     * 게시물 상세 정보
     */
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> postDetail(@PathVariable("postId") Long postId) {

        Long authorId = userService.getPostOwner(postId).getId();

        PostDto postDto = postCRUDService.postDetail(postId, authorId);

        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }
    
    /**
     * 유저가 작성 또는 참가한 게시물 리스트
     */
    @GetMapping("/posts/myPost")
    public ResponseEntity<List<PostListDto>> myPosts() {
        Account currentUser = userService.getCurrentUser();
        List<PostListDto> postListDtos = myPageService.findAllPostsByUserId(currentUser.getId());
        return new ResponseEntity<>(postListDtos, HttpStatus.OK);
    }

    /**
     * 유저가 작성한 게시물 리스트
     */
    @GetMapping("/posts/authoredPost")
    public ResponseEntity<List<PostListDto>> authoredPosts() {
        Account currentUser = userService.getCurrentUser();
        List<PostListDto> postListDtos = myPageService.findAllAuthoredPostByUserId(currentUser.getId());
        return new ResponseEntity<>(postListDtos, HttpStatus.OK);
    }

    /**
     * 유저가 참여한 모임(게시물) 리스트
     */
    @GetMapping("/posts/participatedPost")
    public ResponseEntity<List<PostListDto>> participatedPosts() {
        Account currentUser = userService.getCurrentUser();
        List<PostListDto> postListDtos = myPageService.findAllParticipatedPostByUserId(currentUser.getId());
        return new ResponseEntity<>(postListDtos, HttpStatus.OK);
    }

    /**
     * 게시물 참가 신청
     */
    @PostMapping("/posts/{postId}/join")
    public ResponseEntity<Long> applyJoin(@PathVariable("postId") Long postId) {
        Long currentUserId = userService.getCurrentUser().getId();
        postJoinService.applyJoin(currentUserId, postId);
        return new ResponseEntity<>(currentUserId, HttpStatus.OK);
    }

    /**
     * 참가 취소
     */
    @DeleteMapping("/posts/{postId}/join")
    public ResponseEntity<Long> cancelJoin(@PathVariable("postId") Long postId) {
        Long currentUserId = userService.getCurrentUser().getId();
        postJoinService.cancelJoin(currentUserId, postId);
        return new ResponseEntity<>(currentUserId, HttpStatus.OK);
    }
}


