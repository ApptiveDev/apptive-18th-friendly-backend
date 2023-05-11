package apptive.team1.friendly.domain.post.service;

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

//    @Transactional
//    public void update(Long postId, Post post) {
//        Post findOne = postRepository.findOneById(postId);
//        findOne.update(post);
//    }


    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post findOne(Long id) {
        return postRepository.findOneById(id);
    }
}
