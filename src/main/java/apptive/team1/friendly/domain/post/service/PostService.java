package apptive.team1.friendly.domain.post.service;
import apptive.team1.friendly.domain.post.dto.*;
import apptive.team1.friendly.domain.post.entity.AccountPost;
import apptive.team1.friendly.domain.post.entity.Comment;
import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.post.entity.PostImage;
import apptive.team1.friendly.domain.post.repository.AccountPostRepository;
import apptive.team1.friendly.domain.post.repository.PostRepository;
import apptive.team1.friendly.domain.user.data.dto.PostOwnerInfo;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.repository.*;
import apptive.team1.friendly.global.common.s3.AwsS3Uploader;
import apptive.team1.friendly.global.common.s3.FileInfo;
import apptive.team1.friendly.global.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final AccountPostRepository accountPostRepository;
    private final AwsS3Uploader awsS3Uploader;

    // 조회(READ)
    /**
     * 모든 게시물 찾아서 Post List DTO로 변환
     */
    public List<PostListDto> findAll() {
        List<Post> posts = postRepository.findAll();
        List<PostListDto> postListDtos = new ArrayList<>();
        for (Post post : posts) {
            PostListDto postListDto = PostListDto.builder()
                    .title(post.getTitle())
                    .maxPeople(post.getMaxPeople())
                    .hashTags(post.getHashTag())
                    .promiseTime(post.getPromiseTime())
                    .description(post.getDescription())
                    .location(post.getLocation())
                    .build();

            // 대표 이미지 설정. 기본 이미지가 존재하기 때문에 예외처리 하지 않음
            PostImageDto postImageDto = PostImageDto.builder()
                    .originalFileName(post.getPostImages().get(0).getOriginalFileName())
                    .uploadFileUrl(post.getPostImages().get(0).getUploadFileUrl())
                    .uploadFileName(post.getPostImages().get(0).getUploadFileName())
                    .uploadFilePath(post.getPostImages().get(0).getUploadFilePath())
                    .build();

            postListDto.setPostImageDto(postImageDto);

            postListDtos.add(postListDto);
        }
        return postListDtos;
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
    public List<Post> findPostsByUserEmail(String userEmail) {
        return postRepository.findByUser(userEmail);
    }


    // CUD
    /**
     * 게시물 추가하기
     */
    @Transactional
    public Long addPost(PostFormDto postFormDto, List<MultipartFile> multipartFiles) throws IOException {
        // author 찾기
        Account author = SecurityUtil.getCurrentUserName().flatMap(accountRepository::findOneWithAccountAuthoritiesByEmail).orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        Post post = Post.builder()
                .createdDate(LocalDateTime.now())
                .maxPeople(postFormDto.getMaxPeople())
                .title(postFormDto.getTitle())
                .promiseTime(postFormDto.getPromiseTime())
                .description(postFormDto.getDescription())
                .location(postFormDto.getLocation())
                .hashTag(postFormDto.getHashTag())
                .rules(postFormDto.getRules())
                .build();

        postRepository.save(post);
        
        // AWS 이미지 업로드 및 게시물에 이미지 저장
        ImageUpload(multipartFiles, post);

        // AccountPost 생성하고, Account와 연관 관계 설정
        AccountPost newAccountPost = new AccountPost();
        newAccountPost.relateUser(author);

        // AccountPost를 Post와 연결
        newAccountPost.relatePost(post);

        // post 저장, postId 리턴
        accountPostRepository.save(newAccountPost);

        return post.getId();
    }

    /**
     * 게시물 삭제
     */
    @Transactional
    public Long deletePost(Long postId) {
        // author 찾기
        Account author = SecurityUtil.getCurrentUserName().flatMap(accountRepository::findOneWithAccountAuthoritiesByEmail).orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        AccountPost findAccountPost = accountPostRepository.findOneByPostId(postId);
        Post findPost = findAccountPost.getPost();

        // AccountPost에서 user를 찾아서 userId와 비교
        if(!Objects.equals(findPost.getId(), author.getId())) // 본인 게시물이 아니면 삭제 불가
            throw new RuntimeException("접근 권한이 없습니다.");

        // 이미지 모두 삭제
        if(!Objects.equals(findPost.getPostImages().get(0).getOriginalFileName(), "기본 이미지 이름 예시")) { // 기본 이미지가 아니면 삭제
            List<PostImage> postImages = findPost.getPostImages();
            for(PostImage postImage : postImages) {
                awsS3Uploader.delete(postImage.getOriginalFileName());
            }
        }

        // 삭제한 post의 id 리턴
        return accountPostRepository.delete(postId);

    }

    /**
     * 게시물 수정(업데이트)
     */
    @Transactional
    public Long updatePost(Long postId, PostFormDto updateForm, List<MultipartFile> multipartFiles) throws IOException {
        // author 찾기
        Account author = SecurityUtil.getCurrentUserName().flatMap(accountRepository::findOneWithAccountAuthoritiesByEmail).orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        // postId에 해당하는 AccountPost 찾기
        AccountPost findAccountPost = accountPostRepository.findOneByPostId(postId);

        // 현재 로그인된 user와 게시글의 user가 다르면 예외 처리
        if(author.getId() != findAccountPost.getUser().getId())
            throw new RuntimeException("권한이 없습니다."); // 본인 게시물 아니면 수정 불가

        // AccountPost와 연관된 post 찾음
        Post findPost = findAccountPost.getPost();

        findPost.update(updateForm);

        // 이미지 모두 삭제
        if(!Objects.equals(findPost.getPostImages().get(0).getOriginalFileName(), "기본 이미지 이름 예시")) { // 기본 이미지가 아니면 삭제
            List<PostImage> postImages = findPost.getPostImages();
            for(PostImage postImage : postImages) {
                awsS3Uploader.delete(postImage.getOriginalFileName());
                postRepository.deleteImage(postImage);
            }
        }
        // AWS에 이미지 업로드 및 게시물에 이미지 저장
        ImageUpload(multipartFiles, findPost);

        accountPostRepository.save(findAccountPost);
        return findPost.getId();
    }
    
    
    /**
     * DTO 설정 메소드
     */
    public PostDto postDetail(Long postId, PostOwnerInfo postOwnerInfo) {
        Post findPost = findByPostId(postId);

        // postDto 설정
        PostDto postDto = new PostDto();
        // user 정보
        postDto.setPostOwnerInfo(postOwnerInfo);
        // 게시글 정보
        postDto.setPostId(findPost.getId());
        postDto.setTitle(findPost.getTitle());
        postDto.setMaxPeople(findPost.getMaxPeople());
        postDto.setDescription(findPost.getDescription());
        postDto.setRules(findPost.getRules());
        postDto.setHashTag(findPost.getHashTag());
        postDto.setPromiseTime(findPost.getPromiseTime());

        // 댓글 목록 설정
        Set<Comment> comments = findPost.getComments();
        Set<CommentDto> commentDtos = new HashSet<>();
        for(Comment c : comments) {
            CommentDto commentDto = new CommentDto();
            commentDto.setUsername(c.getAccount().getFirstName() + c.getAccount().getLastName());
            commentDto.setText(c.getText());
            commentDto.setCreateTime(c.getCreatedDate());
            commentDtos.add(commentDto);
        }
        postDto.setComments(commentDtos);

        return postDto;
    }


    /**
     * 수정 화면을 보여주기 위해 현재 게시물의 내용을 PostFormDto에 담아서 반환
     */
    public PostFormDto getUpdateForm(Long postId) {
        Post post = postRepository.findOneByPostId(postId);
        return PostFormDto.builder()
                .rules(post.getRules())
                .title(post.getTitle())
                .hashTag(post.getHashTag())
                .description(post.getDescription())
                .maxPeople(post.getMaxPeople())
                .promiseTime(post.getPromiseTime())
                .location(post.getLocation())
                .build();
    }

    /**
     * Post에 이미지 업로드
     */
    private void ImageUpload(List<MultipartFile> multipartFiles, Post post) throws IOException {
        for(MultipartFile multipartFile : multipartFiles) {
            FileInfo uploadFile = awsS3Uploader.upload(multipartFile, "post"+ post.getId().toString());
            PostImage postImage = PostImage.builder()
                    .post(post)
                    .originalFileName(uploadFile.getOriginalFileName())
                    .uploadFileName(uploadFile.getUploadFileName())
                    .uploadFilePath(uploadFile.getUploadFileName())
                    .uploadFileUrl(uploadFile.getUploadFileUrl())
                    .build();
            postRepository.saveImage(postImage);
        }
    }
}
