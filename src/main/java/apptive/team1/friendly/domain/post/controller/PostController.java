package apptive.team1.friendly.domain.post.controller;

import apptive.team1.friendly.domain.post.dto.PostDto;
import apptive.team1.friendly.domain.post.dto.PostFormDto;
import apptive.team1.friendly.domain.post.dto.PostListDto;
import apptive.team1.friendly.domain.post.service.PostService;
import apptive.team1.friendly.domain.user.data.dto.AccountInfoResponse;
import apptive.team1.friendly.domain.user.data.dto.PostOwnerInfo;
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

    private final PostService postService;
    private final UserService userService;
    /**
     * 게시물 추가
     */
    @GetMapping("/posts/create") // 게시물 추가 화면 구성
    public ResponseEntity<PostOwnerInfo> addPost() {
        PostOwnerInfo userInfo = userService.getCurrentUserInfo();
        return ResponseEntity.status(HttpStatus.OK).body(userInfo);
    }

    @PostMapping("/posts/create") // 게시물 추가 요청
    public ResponseEntity<Long> addPost(@RequestPart PostFormDto postForm, @RequestPart List<MultipartFile> imageFiles) throws IOException {
        Long postId = postService.addPost(postForm, imageFiles);
        return ResponseEntity.status(HttpStatus.OK).body(postId);
    }


    /**
     * 게시물 삭제
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Long> deletePost(@PathVariable("postId") Long postId) {
        // 게시물 삭제
        Long deletedPostId = postService.deletePost(postId);

        // 삭제된 게시물 개수 반환
        return ResponseEntity.status(HttpStatus.OK).body(deletedPostId);
    }

    /**
     * 게시물 수정
     */
    @GetMapping("/posts/{postId}/edit") // 업데이트 페이지 화면 구성
    public ResponseEntity<PostFormDto> updatePost(@PathVariable("postId") Long postId) {
        PostFormDto updateForm = postService.getUpdateForm(postId);
        return ResponseEntity.status(HttpStatus.OK).body(updateForm);
    }

    @PutMapping("/posts/{postId}/edit") // 업데이트 요청
    public ResponseEntity<Long> updatePost(@PathVariable("postId") Long postId, @RequestPart PostFormDto postForm, @RequestPart List<MultipartFile> imageFiles) throws IOException {
        Long updatedPostId = postService.updatePost(postId, postForm, imageFiles);
        return new ResponseEntity<>(updatedPostId, HttpStatus.OK);
    }


    /**
     * 게시믈 리스트
     */
    @GetMapping("/posts")
    public ResponseEntity<List<PostListDto>> postList() {
        List<PostListDto> postListDtos = postService.findAll();
        return new ResponseEntity<>(postListDtos, HttpStatus.OK);
    }

    /**
     * 게시물 상세 정보
     */
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> postDetail(@PathVariable("postId") Long postId) {
        PostOwnerInfo postOwnerInfo = userService.getPostOwnerInfo(postId);
        PostDto postDto = postService.postDetail(postId, postOwnerInfo);

        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }
}

