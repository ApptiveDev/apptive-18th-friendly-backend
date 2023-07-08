package apptive.team1.friendly.domain.post.controller;

import apptive.team1.friendly.domain.post.dto.PostDto;
import apptive.team1.friendly.domain.post.dto.PostFormDto;
import apptive.team1.friendly.domain.post.dto.PostListDto;
import apptive.team1.friendly.domain.post.service.PostService;
import apptive.team1.friendly.domain.post.vo.AudioGuide;
import apptive.team1.friendly.domain.user.data.dto.PostOwnerInfo;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.service.UserService;
import apptive.team1.friendly.global.config.ObjectMapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
public class PostController {

    private final PostService postService;

    private final UserService userService;

    @Value("${travelAPI.encoding-key}")
    private String apikey;

    @Value("${travelAPI.mobileOS}")
    private String mobileOS;

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
        Account author = userService.getCurrentUser();
        Long postId = postService.addPost(author, postForm, imageFiles);
        return ResponseEntity.status(HttpStatus.OK).body(postId);
    }


    /**
     * 게시물 삭제
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Long> deletePost(@PathVariable("postId") Long postId) {
        Account author = userService.getCurrentUser();
        // 게시물 삭제
        Long deletedPostId = postService.deletePost(author, postId);

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
        Account author = userService.getCurrentUser();
        Long updatedPostId = postService.updatePost(author, postId, postForm, imageFiles);
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

    /**
     * 오디오 가이드 API 호출
     */
    @GetMapping("/posts/audioGuide")
    public Mono<List<AudioGuide>> getAudioGuides(@RequestParam("locationName") String locationName, @RequestParam("languageCode") String languageCode) throws URISyntaxException {
        WebClient webClient = WebClient.create();
        URI uri = new URI("https://apis.data.go.kr/B551011/Odii/storySearchList" +
                "?MobileOS=" + mobileOS + "&keyword=" + locationName + "&MobileApp=Photour" +
                "&serviceKey=" + apikey +
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
}


