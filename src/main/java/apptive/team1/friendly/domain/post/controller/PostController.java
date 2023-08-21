package apptive.team1.friendly.domain.post.controller;

import apptive.team1.friendly.domain.post.dto.PostDto;
import apptive.team1.friendly.domain.post.dto.PostFormDto;
import apptive.team1.friendly.domain.post.dto.PostListDto;
import apptive.team1.friendly.domain.post.service.PostService;
import apptive.team1.friendly.domain.post.vo.AudioGuide;
import apptive.team1.friendly.domain.user.data.dto.UserInfo;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.service.UserService;
import apptive.team1.friendly.global.baseEntity.ApiBase;
import apptive.team1.friendly.global.utils.ObjectMapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController extends ApiBase {

    private final PostService postService;

    private final UserService userService;

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
        Account author = userService.getCurrentUser();
        Long postId = postService.addPost(author, postForm, imageFiles);
        return ResponseEntity.status(HttpStatus.OK).body(postId);
    }

    /**
     * 게시물 삭제
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Long> deletePost(@PathVariable("postId") Long postId) {
        Account currentUser = userService.getCurrentUser();
        // 게시물 삭제
        Long deletedPostId = postService.deletePost(currentUser, postId);

        // 삭제된 게시물 개수 반환
        return ResponseEntity.status(HttpStatus.OK).body(deletedPostId);
    }

    /**
     * 게시물 수정
     */
    @GetMapping("/posts/{postId}/edit") // 업데이트 페이지 화면 구성
    public ResponseEntity<PostFormDto> updatePostForm(@PathVariable("postId") Long postId) {
        PostFormDto updateForm = postService.getUpdateForm(postId);
        return ResponseEntity.status(HttpStatus.OK).body(updateForm);
    }

    @PutMapping("/posts/{postId}/edit") // 게시물 업데이트
    public ResponseEntity<Long> updatePost(@PathVariable("postId") Long postId, @RequestPart PostFormDto postForm, @RequestPart List<MultipartFile> imageFiles) throws IOException {
        Account currentUser = userService.getCurrentUser();
        Long updatedPostId = postService.updatePost(currentUser, postId, postForm, imageFiles);
        return new ResponseEntity<>(updatedPostId, HttpStatus.OK);
    }

    /**
     * 게시믈 리스트 검색
     */
    @GetMapping("/posts" )
    public ResponseEntity<List<PostListDto>> postList(@RequestParam(required = false) String tag, @RequestParam(required = false) String keyword) {
        List<PostListDto> postListDtos = postService.findAll(tag, keyword);
        return new ResponseEntity<>(postListDtos, HttpStatus.OK);
    }

    /**
     * 유저가 작성한 게시물 리스트
     */
    @GetMapping("/posts/mypost")
    public ResponseEntity<List<PostListDto>> myPostList() {
        Account currentUser = userService.getCurrentUser();
        List<PostListDto> postListDtos = postService.findAllByUserId(currentUser.getId());
        return new ResponseEntity<>(postListDtos, HttpStatus.OK);
    }

    /**
     * 게시물 상세 정보
     */
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> postDetail(@PathVariable("postId") Long postId) {

        Account currentUser = userService.getCurrentUser();

        Account author = userService.getPostOwner(postId);

        PostDto postDto = postService.postDetail(postId, currentUser, author);

        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    /**
     * 오디오 가이드 API 호출
     */
    @GetMapping("/posts/audioGuide")
    public Mono<List<AudioGuide>> getAudioGuides(@RequestParam("locationName") String locationName, @RequestParam("languageCode") String languageCode) throws URISyntaxException {
        WebClient webClient = WebClient.create();
        URI uri = new URI("https://apis.data.go.kr/B551011/Odii/storySearchList" +
                "?MobileOS=" + getMobileOS() + "&keyword=" + locationName + "&MobileApp=Photour" +
                "&serviceKey=" + getApikey() +
                "&_type=json&langCode=" + languageCode);

        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(jsonResponse -> {
                    JsonNode jsonNode;
                    ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();
                    try {
                        jsonNode = objectMapper.readTree(jsonResponse).get("response").get("body").get("items").get("item");
                        List<AudioGuide> audioGuides = objectMapper.readValue(jsonNode.toString(), new TypeReference<List<AudioGuide>>() {});
                        return Mono.just(audioGuides);
                    } catch (JsonProcessingException e) {
                        return Mono.error(e);
                    }
                })
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(new ArrayList<>());
                });
    }

    /**
     * 게시물 참가 신청
     */
    @PostMapping("/posts/join/{postId}")
    public ResponseEntity<Long> applyJoin(@PathVariable("postId") Long postId) {
        Account currentUser = userService.getCurrentUser();
        postService.applyJoin(currentUser, postId);
        return new ResponseEntity<>(currentUser.getId(), HttpStatus.OK);
    }

    /**
     * 참가 취소
     */
    @DeleteMapping("/posts/join/{postId}")
    public ResponseEntity<Long> cancelJoin(@PathVariable("postId") Long postId) {
        Account currentUser = userService.getCurrentUser();
        postService.cancelJoin(currentUser, postId);
        return new ResponseEntity<>(currentUser.getId(), HttpStatus.OK);
    }
}


