package apptive.team1.friendly.domain.post.service;

import apptive.team1.friendly.domain.post.dto.PostDto;
import apptive.team1.friendly.domain.post.dto.UpdatePostDto;
import apptive.team1.friendly.domain.post.entity.AccountPost;
import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void save(Post post) {
        postRepository.save(post);
    }

    @Transactional
    public void update(Long postId, UpdatePostDto postDto) {
        Post findPost = postRepository.findOneByPostId(postId);
        findPost.change(postDto);
    }


    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post findByPostId(Long id) {
        return postRepository.findOneByPostId(id);
    }

    public List<Post> findByUser(Long userId) {
        return postRepository.findByUser(userId);
    }

    public Long addPost(Long authorId, PostDto postDto) {
        // author 찾기

        // Post 객체 생성 후 postDto 값 옮김

        // Post객체 저장
    }
}
