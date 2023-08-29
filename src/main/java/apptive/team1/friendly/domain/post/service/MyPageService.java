package apptive.team1.friendly.domain.post.service;

import apptive.team1.friendly.domain.post.dto.PostListDto;
import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final PostRepository postRepository;
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
}
