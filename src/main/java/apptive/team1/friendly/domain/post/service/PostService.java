package apptive.team1.friendly.domain.post.service;
import apptive.team1.friendly.domain.post.dto.*;
import apptive.team1.friendly.domain.post.entity.*;
import apptive.team1.friendly.domain.post.exception.AccessDeniedException;
import apptive.team1.friendly.domain.post.repository.PostRepository;
import apptive.team1.friendly.domain.user.data.dto.PostOwnerInfo;
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
    private final AccountRepository accountRepository;
    private final AwsS3Uploader awsS3Uploader;

    // 조회(READ)
    /**
     * 모든 게시물 찾아서 Post List DTO로 변환
     */
    public List<PostListDto> findAll() {
        List<Post> posts = postRepository.findAll();
        List<PostListDto> postListDtos = PostListDto.createPostListDto(posts);
        return postListDtos;
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
        Account author = accountRepository.findAuthorByPostId(postId);

        isHasAuthority(currentUser, author); // 본인 게시물이 아니면 삭제 불가

        findPost.deleteImages(awsS3Uploader);

        return postRepository.delete(findPost);
    }


    /**
     * 게시물 수정(업데이트)
     */
    @Transactional
    public Long updatePost(Account currentUser, Long postId, PostFormDto updateForm, List<MultipartFile> multipartFiles) throws IOException {
        Post findPost = postRepository.findOneByPostId(postId);
        Account author = accountRepository.findAuthorByPostId(postId);

        isHasAuthority(currentUser, author); // 본인 게시물 아니면 수정 불가

        findPost.update(updateForm);

        findPost.deleteImages(awsS3Uploader);

        findPost.uploadImages(multipartFiles, awsS3Uploader);

        return findPost.getId();
    }


    /**
     * DTO 설정 메소드
     */
    public PostDto postDetail(Long postId, PostOwnerInfo postOwnerInfo) {
        Post findPost = findByPostId(postId);
        return PostDto.createPostDto(findPost, postOwnerInfo);
    }


    /**
     * 수정 화면을 보여주기 위해 현재 게시물의 내용을 PostFormDto에 담아서 반환
     */
    public PostFormDto getUpdateForm(Long postId) {
        Post post = postRepository.findOneByPostId(postId);
        return PostFormDto.createPostFormDto(post);
    }

    /**
     * 게시물 수정/삭제 권한 확인
     */
    private void isHasAuthority(Account currentUser, Account author) {
        if(!Objects.equals(author.getId(), currentUser.getId()))
            throw new AccessDeniedException("접근 권한이 없습니다.");
    }
}
