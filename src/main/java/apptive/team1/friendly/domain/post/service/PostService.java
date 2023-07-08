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
import apptive.team1.friendly.global.common.s3.AwsS3Uploader;
import apptive.team1.friendly.global.common.s3.FileInfo;
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
    public List<Post> findPostsByUserEmail(String userEmail) {
        return postRepository.findByUser(userEmail);
    }


    // CUD
    /**
     * 게시물 추가하기
     */
    @Transactional
    public Long addPost(Account author, PostFormDto postFormDto, List<MultipartFile> multipartFiles) throws IOException {
        Post post = Post.builder()
                .createdDate(LocalDateTime.now())
                .maxPeople(postFormDto.getMaxPeople())
                .title(postFormDto.getTitle())
                .promiseTime(postFormDto.getPromiseTime())
                .description(postFormDto.getDescription())
                .location(postFormDto.getLocation())
                .hashTags(postFormDto.getHashTag())
                .rules(postFormDto.getRules())
                .audioGuide(postFormDto.getAudioGuide())
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
    public Long deletePost(Account author, Long postId) {
        AccountPost findAccountPost = accountPostRepository.findOneByPostId(postId);
        Post findPost = findAccountPost.getPost();

        // AccountPost에서 user를 찾아서 userId와 비교
        if(!Objects.equals(findAccountPost.getUser().getId(), author.getId())) // 본인 게시물이 아니면 삭제 불가
            throw new RuntimeException("접근 권한이 없습니다.");

        // 이미지 모두 삭제
        if(findPost.getPostImages().size() > 0) {
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
    public Long updatePost(Account author, Long postId, PostFormDto updateForm, List<MultipartFile> multipartFiles) throws IOException {
        // postId에 해당하는 AccountPost 찾기
        AccountPost findAccountPost = accountPostRepository.findOneByPostId(postId);

        // 현재 로그인된 user와 게시글의 user가 다르면 예외 처리
        if(!Objects.equals(author.getId(), findAccountPost.getUser().getId()))
            throw new RuntimeException("권한이 없습니다."); // 본인 게시물 아니면 수정 불가

        // AccountPost와 연관된 post 찾음
        Post findPost = findAccountPost.getPost();

        findPost.update(updateForm);

        // 이미지 모두 삭제
        if(findPost.getPostImages().size() > 0) {
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
                .hashTag(findPost.getHashTags())
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
            postRepository.saveImage(postImage);
        }
    }
}
