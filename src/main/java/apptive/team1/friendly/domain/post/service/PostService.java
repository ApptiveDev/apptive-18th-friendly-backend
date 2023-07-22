package apptive.team1.friendly.domain.post.service;
import apptive.team1.friendly.domain.post.dto.*;
import apptive.team1.friendly.domain.post.entity.*;
import apptive.team1.friendly.domain.post.exception.AccessDeniedException;
import apptive.team1.friendly.domain.post.exception.InvalidHashTagException;
import apptive.team1.friendly.domain.post.repository.PostRepository;
import apptive.team1.friendly.domain.user.data.dto.UserInfo;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
import apptive.team1.friendly.global.common.s3.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final AwsS3Uploader awsS3Uploader;

    // 조회(READ)
    /**
     * 모든 게시물 찾아서 PostList DTO로 변환
     */
    public List<PostListDto> findAll() {
        List<Post> posts = postRepository.findAll();
        return PostListDto.createPostListDto(posts);
    }
    /**
     * 해당 HashTag 게시물 찾아서 PostList DTO로 변환
     */
    public List<PostListDto> findByHashTag(String tag) {
        validateTag(tag);
        List<Post> posts = postRepository.findByHashTag(tag);
        return PostListDto.createPostListDto(posts);
    }

    private void validateTag(String tag) {
        if(tag.equals("NATIVE") || tag.equals("LIFE") || tag.equals("FAMOUS") || tag.equals("HOTPLACE")) {
            ;
        }
        else {
            throw new InvalidHashTagException("유효하지 않은 해시태그");
        }
    }

    /**
     * postId로 해당 게시물 찾기
     */
    public Post findByPostId(Long id) {
        return postRepository.findOneByPostId(id);
    }

    /**
     * userEmail로 user가 쓴 게시물 리스트 찾기
     */
    public List<Post> findPostsByUserId(Long userId) {
        return postRepository.findByUser(userId);
    }


    // CUD
    /**
     * 게시물 추가하기
     */
    @Transactional
    public Long addPost(Account author, PostFormDto postFormDto, List<MultipartFile> multipartFiles) throws IOException {
        Post post = Post.createPost(author, postFormDto);

        postRepository.save(post);

        post.uploadImages(multipartFiles, awsS3Uploader);

        return post.getId();
    }

    /**
     * 게시물 삭제
     */
    @Transactional
    public Long deletePost(Account currentUser, Long postId) {
        Post findPost = postRepository.findOneByPostId(postId);

        findPost.deleteImages(currentUser, awsS3Uploader);

        return postRepository.delete(findPost);
    }


    /**
     * 게시물 수정(업데이트)
     */
    @Transactional
    public Long updatePost(Account currentUser, Long postId, PostFormDto updateForm, List<MultipartFile> multipartFiles) throws IOException {
        Post findPost = postRepository.findOneByPostId(postId);

        findPost.update(currentUser, updateForm);

        findPost.deleteImages(currentUser, awsS3Uploader); // 저장된 이미지를 모두 삭제

        findPost.uploadImages(multipartFiles, awsS3Uploader);

        return findPost.getId();
    }

    /**
     * 같이가요 참가 신청
     */
    @Transactional
    public void applyJoin(Account currentUser, Long postId) {
        Post findPost = postRepository.findOneByPostId(postId);
        findPost.addParticipant(currentUser);
    }


    /**
     * DTO 설정 메소드
     */
    public PostDto postDetail(Long postId, UserInfo userInfo) {
        Post findPost = findByPostId(postId);
        return PostDto.createPostDto(findPost, userInfo);
    }


    /**
     * 수정 화면을 보여주기 위해 현재 게시물의 내용을 PostFormDto에 담아서 반환
     */
    public PostFormDto getUpdateForm(Long postId) {
        Post post = postRepository.findOneByPostId(postId);
        return PostFormDto.createPostFormDto(post);
    }

}
