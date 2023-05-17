package apptive.team1.friendly.domain.post.controller;

import apptive.team1.friendly.domain.post.dto.PostDto;
import apptive.team1.friendly.domain.post.dto.PostListDto;
import apptive.team1.friendly.domain.post.entity.AccountPost;
import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.post.service.PostService;
import apptive.team1.friendly.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 같이가요! 게시물 추가
     */
    @PostMapping("/posts")
    public void addPost(@RequestBody PostDto postDto) {
        Long authorId = postDto.getAuthorId();
        postService.addPost()

    }

    // 수정

    // 삭제

    /**
     * 같이가요! 게시믈 리스트
     */
    @GetMapping("/posts")
    public ResponseEntity<List<PostListDto>> list() {
        List<Post> posts = postService.findAll();
        List<PostListDto> postListDtos = new ArrayList<PostListDto>();
        for (Post post : posts) {
            PostListDto postListDto = new PostListDto();
            postListDto.setTitle(post.getTitle());
            postListDto.setMaxPeople(post.getMaxPeople());
            postListDto.setHashTag(post.getHashTag());
            postListDto.setPromiseTime(post.getPromiseTime());
            postListDto.setImage(post.getImage());

            postListDtos.add(postListDto);
        }
        return new ResponseEntity<>(postListDtos, HttpStatus.OK);
    }

    /**
     * 같이가요! 게시물 상세 정보
     */
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> PostDetail(@PathVariable("postId") Long postId) {
        Post findPost = postService.findByPostId(postId);
        PostDto postDto = new PostDto();
        postDto.setTitle(findPost.getTitle());
        postDto.setMaxPeople(findPost.getMaxPeople());
        postDto.setDescription(findPost.getDescription());
        postDto.setRules(findPost.getRules());
        postDto.setHashTag(findPost.getHashTag());
        postDto.setPromiseTime(findPost.getPromiseTime());

        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

}
