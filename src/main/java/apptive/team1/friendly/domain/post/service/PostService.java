package apptive.team1.friendly.domain.post.service;
import apptive.team1.friendly.domain.post.dto.*;
import apptive.team1.friendly.domain.post.entity.*;
import apptive.team1.friendly.domain.post.repository.PostRepository;
import apptive.team1.friendly.domain.user.data.dto.PostOwnerInfo;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
import apptive.team1.friendly.global.common.s3.AwsS3Uploader;
import apptive.team1.friendly.global.common.s3.FileInfo;
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
        List<PostListDto> postListDtos = new ArrayList<>();
        for (Post post : posts) {
            PostListDto postListDto = PostListDto.builder()
                    .postId(post.getId())
                    .title(post.getTitle())
                    .maxPeople(post.getMaxPeople())
                    .hashTags(post.getHashTags())
                    .promiseTime(post.getPromiseTime())
                    .description(post.getDescription())
                    .location(post.getLocation())
                    .build();

            // 대표 이미지 설정
            if(post.getPostImages().size() > 0) {
                PostImageDto postImageDto = PostImageDto.builder()
                        .originalFileName(post.getPostImages().get(0).getOriginalFileName())
                        .uploadFileUrl(post.getPostImages().get(0).getUploadFileUrl())
                        .uploadFileName(post.getPostImages().get(0).getUploadFileName())
                        .uploadFilePath(post.getPostImages().get(0).getUploadFilePath())
                        .build();
                postListDto.setPostImageDto(postImageDto);
            }

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
        AccountPost accountPost = AccountPost.createAccountPost(author, AccountType.AUTHOR);
        Post post = Post.createPost(postFormDto, accountPost);
        // ImageUpload 함수 내부에서 postId를 사용하기 때문에 Id를 얻기 위해 persist
        postRepository.save(post);
        
        // AWS 이미지 업로드 및 게시물에 이미지 저장
        ImageUpload(multipartFiles, post);

        return post.getId();
    }

    /**
     * 게시물 삭제
     */
    @Transactional
    public Long deletePost(Account currentUser, Long postId) {
        Post findPost = postRepository.findOneByPostId(postId);
        Account author = accountRepository.findAuthorByPostId(postId);

        // AccountPost에서 첫 번째 user(방 생성자)를 찾아서 userId와 비교
        if(!Objects.equals(author.getId(), currentUser.getId())) // 본인 게시물이 아니면 삭제 불가
            throw new RuntimeException("접근 권한이 없습니다.");

        // 이미지 모두 삭제
        List<PostImage> postImages = findPost.getPostImages();
        if(postImages.size() > 0) {
            for(int i=postImages.size()-1; i>=0; i--) {
                awsS3Uploader.delete(postImages.get(i).getOriginalFileName());
            }
        }

        // 삭제한 post의 id 리턴
        return postRepository.delete(findPost);

    }

    /**
     * 게시물 수정(업데이트)
     */
    @Transactional
    public Long updatePost(Account currentUser, Long postId, PostFormDto updateForm, List<MultipartFile> multipartFiles) throws IOException {
        Post findPost = postRepository.findOneByPostId(postId);
        Account author = accountRepository.findAuthorByPostId(postId);

        // 현재 로그인된 user와 게시글의 user가 다르면 예외 처리
        if(!Objects.equals(author.getId(), currentUser.getId()))
            throw new RuntimeException("권한이 없습니다."); // 본인 게시물 아니면 수정 불가


        findPost.update(updateForm);

        List<PostImage> postImages = findPost.getPostImages();
        // 이미지 모두 삭제
        if(postImages.size() > 0) {
            for(int i=postImages.size()-1; i>=0; i--) {
                awsS3Uploader.delete(postImages.get(i).getOriginalFileName());
                findPost.deleteImage(postImages.get(i));
            }
        }
        // AWS에 이미지 업로드 및 게시물에 이미지 저장
        ImageUpload(multipartFiles, findPost);

        return findPost.getId();
    }
    
    
    /**
     * DTO 설정 메소드
     */
    public PostDto postDetail(Long postId, PostOwnerInfo postOwnerInfo) {
        Post findPost = findByPostId(postId);
        Set<Comment> comments = findPost.getComments();

        Set<CommentDto> commentDtos = new HashSet<>();
        // 댓글 목록 설정
        if(comments.size() > 0) {
            for (Comment c : comments) {
                CommentDto commentDto = new CommentDto();
                commentDto.setUsername(c.getAccount().getFirstName() + c.getAccount().getLastName());
                commentDto.setText(c.getText());
                commentDto.setCreateTime(c.getCreatedDate());
                commentDtos.add(commentDto);
            }
        }

        return PostDto.builder()
                .postOwnerInfo(postOwnerInfo)
                .postId(postId)
                .title(findPost.getTitle())
                .maxPeople(findPost.getMaxPeople())
                .description(findPost.getDescription())
                .rules(findPost.getRules())
                .hashTags(findPost.getHashTags())
                .promiseTime(findPost.getPromiseTime())
                .audioGuide(findPost.getAudioGuide())
                .comments(commentDtos)
                .build();
    }


    /**
     * 수정 화면을 보여주기 위해 현재 게시물의 내용을 PostFormDto에 담아서 반환
     */
    public PostFormDto getUpdateForm(Long postId) {
        Post post = postRepository.findOneByPostId(postId);
        return PostFormDto.builder()
                .rules(post.getRules())
                .title(post.getTitle())
                .hashTag(post.getHashTags())
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
                    .post(post) // 연관관계 설정
                    .originalFileName(uploadFile.getOriginalFileName())
                    .uploadFileName(uploadFile.getUploadFileName())
                    .uploadFilePath(uploadFile.getUploadFileName())
                    .uploadFileUrl(uploadFile.getUploadFileUrl())
                    .build();
            post.getPostImages().add(postImage);
        }
    }
}
