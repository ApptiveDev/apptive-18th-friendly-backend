package apptive.team1.friendly.PostService;

import apptive.team1.friendly.domain.post.entity.AccountPost;
import apptive.team1.friendly.domain.post.entity.HashTag;
import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.post.repository.PostRepository;
import apptive.team1.friendly.domain.post.service.PostService;
import apptive.team1.friendly.domain.user.data.entity.Account;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static apptive.team1.friendly.domain.post.entity.HashTag.LIFE;
import static apptive.team1.friendly.domain.post.entity.HashTag.NATIVE;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(value = false)
public class PostServiceTest {

    @Autowired PostService postService;
    @Autowired PostRepository postRepository;

    @Test
    public void 게시물_추가() {
        AccountPost accountPost = new AccountPost();
        List<String> rules = new ArrayList<String>();
        rules.add("rule1");
        rules.add("rule2");
        List<HashTag> hashTag = new ArrayList<HashTag>();
        hashTag.add(LIFE);
        hashTag.add(NATIVE);

        Post newPost = new Post(accountPost, "title", "description", 5, LocalDateTime.now(), "pusan", rules, hashTag);

        AccountPost accountPost2 = new AccountPost();
        Post newPost2 = new Post(accountPost2, "title2", "description2", 3, LocalDateTime.now(), "usan", rules, hashTag);
        postService.save(newPost);
        postService.save(newPost2);

        System.out.println("post.getId() = " + newPost.getId());
        Assert.assertEquals(newPost, postRepository.findOneByPostId(newPost.getId()));
    }
    
    @Test
    public void 게시물_리스트_조회() {
        AccountPost accountPost = new AccountPost();
        List<String> rules = new ArrayList<String>();
        rules.add("rule1");
        rules.add("rule2");
        List<HashTag> hashTag = new ArrayList<HashTag>();
        hashTag.add(LIFE);
        hashTag.add(NATIVE);

        Post newPost = new Post(accountPost, "title", "description", 5, LocalDateTime.now(), "pusan", rules, hashTag);

        AccountPost accountPost2 = new AccountPost();
        Post newPost2 = new Post(accountPost2, "title2", "description2", 3, LocalDateTime.now(), "usan", rules, hashTag);
        postService.save(newPost);
        postService.save(newPost2);

        List<Post> posts = postService.findAll();
//        Assert.assertEquals(2, posts.size());
    }

    @Test
    public void 게시물_조회() {
        AccountPost accountPost = new AccountPost();
        List<String> rules = new ArrayList<String>();
        rules.add("rule1");
        rules.add("rule2");
        List<HashTag> hashTag = new ArrayList<HashTag>();
        hashTag.add(LIFE);
        hashTag.add(NATIVE);

        Post newPost = new Post(accountPost, "title", "description", 5, LocalDateTime.now(), "pusan", rules, hashTag);

        AccountPost accountPost2 = new AccountPost();
        Post newPost2 = new Post(accountPost2, "title2", "description2", 3, LocalDateTime.now(), "usan", rules, hashTag);
        postService.save(newPost);
        postService.save(newPost2);

        Post findPost = postService.findOne(newPost.getId());

        Assert.assertEquals(findPost, newPost);
    }

    @Test
    public void 유저_게시물_조회() {
        Account account = new Account();

    }
}
