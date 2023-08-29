package apptive.team1.friendly.domain.post.service;

import apptive.team1.friendly.domain.post.dto.PostDto;
import apptive.team1.friendly.domain.post.dto.PostFormDto;
import apptive.team1.friendly.domain.post.dto.PostListDto;
import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.post.repository.PostRepository;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
import apptive.team1.friendly.global.common.s3.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static apptive.team1.friendly.domain.post.service.PostServiceHelper.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostCRUDService {

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
     * userId로 user가 쓴 게시물 리스트 찾기
     */
    public List<Post> findPostsByUserId(Long userId) {
        return postRepository.findByUser(userId);
    }

    /**
     * 상세페이지 조회
     */
    public PostDto postDetail(Long postId, Long currentUserId, Long authorId) {

        Account currentUser = findExistingMember(accountRepository, currentUserId);

        Account author = findExistingMember(accountRepository, authorId);

        Post findPost = postRepository.findOneByPostId(postId);

        return PostDto.createPostDto(findPost, currentUser, author);
    }

    /**
     * 게시물 추가하기
     */
    @Transactional
    public Long addPost(Long currentUserId, PostFormDto postFormDto, List<MultipartFile> multipartFiles) throws IOException {

        Account author = findExistingMember(accountRepository, currentUserId);

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

        Account currentUser = findExistingMember(accountRepository, currentUserId);

        Post findPost = postRepository.findOneByPostId(postId);

        findPost.deleteImages(currentUser, awsS3Uploader);

        return postRepository.delete(findPost);
    }

    /**
     * 게시물 수정
     */
    @Transactional
    public Long updatePost(Long currentUserId, Long postId, PostFormDto updateForm, List<MultipartFile> multipartFiles) throws IOException {

        Account currentUser = findExistingMember(accountRepository, currentUserId);

        Post findPost = postRepository.findOneByPostId(postId);

        findPost.update(currentUser, updateForm);

        findPost.deleteImages(currentUser, awsS3Uploader); // 저장된 이미지를 모두 삭제

        findPost.uploadImages(multipartFiles, awsS3Uploader);

        return findPost.getId();
    }


    /**
     * 수정 화면 form 생성
     */
    public PostFormDto getUpdateForm(Long postId) {

        Post post = postRepository.findOneByPostId(postId);

        return PostFormDto.createPostFormDto(post);
    }
}
