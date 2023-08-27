package apptive.team1.friendly.domain.post.service;
import apptive.team1.friendly.domain.post.dto.*;
import apptive.team1.friendly.domain.post.entity.*;
import apptive.team1.friendly.domain.post.repository.PostRepository;
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
     * 모든 게시물 찾아서 PostList DTO로 변환
     */
    public List<PostListDto> findAll(String tag, String keyword) {
        List<Post> posts = postRepository.findAll(tag, keyword);
        return PostListDto.createPostListDto(posts);
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

    /**
     * user가 작성 또는 참여한 게시물 리스트 찾기
     * @param userId 유저 id
     * @return 게시물 리스트
     */
    public List<PostListDto> findAllPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findByUser(userId);
        return PostListDto.createPostListDto(posts);
    }

    /**
     * user가 작성한 게시물 리스트 찾기
     * @param userId 유저 id
     * @return  해당 유저가 작성한 게시물 리스트
     */
    public List<PostListDto> findAllAuthoredPostByUserId(Long userId) {
        List<Post> posts = postRepository.findByAuthor(userId);
        return PostListDto.createPostListDto(posts);
    }

    /**
     * user가 참여한 모임 리스트 찾기
     * @param userId 유저 id
     * @return 해당 유저가 참여한 게시물 리스트
     */
    public List<PostListDto> findAllParticipatedPostByUserId(Long userId) {
        List<Post> posts = postRepository.findByParticipant(userId);
        return PostListDto.createPostListDto(posts);
    }

    /**
     * 게시물 추가하기
     */
    @Transactional
    public Long addPost(Long currentUserId, PostFormDto postFormDto, List<MultipartFile> multipartFiles) throws IOException {

        Account author = findExistingMember(currentUserId);

        Post post = Post.createPost(author, postFormDto);

        postRepository.save(post);

        post.uploadImages(multipartFiles, awsS3Uploader);

        return post.getId();
    }

    /**
     * 게시물 삭제
     */
    @Transactional
    public Long deletePost(Long currentUserId, Long postId) {

        Account currentUser = findExistingMember(currentUserId);

        Post findPost = postRepository.findOneByPostId(postId);

        findPost.deleteImages(currentUser, awsS3Uploader);

        return postRepository.delete(findPost);
    }

    /**
     * 게시물 수정(업데이트)
     */
    @Transactional
    public Long updatePost(Long currentUserId, Long postId, PostFormDto updateForm, List<MultipartFile> multipartFiles) throws IOException {

        Account currentUser = findExistingMember(currentUserId);

        Post findPost = postRepository.findOneByPostId(postId);

        findPost.update(currentUser, updateForm);

        findPost.deleteImages(currentUser, awsS3Uploader); // 저장된 이미지를 모두 삭제

        findPost.uploadImages(multipartFiles, awsS3Uploader);

        return findPost.getId();
    }

    /**
     * DTO 설정 메소드
     */
    public PostDto postDetail(Long postId, Long currentUserId, Long authorId) {

        Account currentUser = findExistingMember(currentUserId);

        Account author = findExistingMember(authorId);

        Post findPost = postRepository.findOneByPostId(postId);

        return PostDto.createPostDto(findPost, currentUser, author);
    }

    /**
     * 수정 화면을 보여주기 위해 현재 게시물의 내용을 PostFormDto에 담아서 반환
     */
    public PostFormDto getUpdateForm(Long postId) {

        Post post = postRepository.findOneByPostId(postId);

        return PostFormDto.createPostFormDto(post);
    }


    //================= 참가 신청관련 서비스 =================//

    /**
     * 같이가요 참가 신청
     */
    @Transactional
    public void applyJoin(Long currentUserId, Long postId) {

        Account currentUser = findExistingMember(currentUserId);

        Post findPost = postRepository.findOneByPostId(postId);

        findPost.addParticipant(currentUser);
    }

    @Transactional
    public void cancelJoin(Long currentUserId, Long postId) {

        Account currentUser = findExistingMember(currentUserId);

        Post findPost = postRepository.findOneByPostId(postId);

        findPost.deleteParticipant(currentUser);
    }

    private Account findExistingMember(Long accountId) {

        Optional<Account> authorOptional = accountRepository.findOneWithAccountAuthoritiesById(accountId);

        if(!authorOptional.isPresent()) throw new RuntimeException("존재하지 않는 user");

        return authorOptional.get();
    }
}