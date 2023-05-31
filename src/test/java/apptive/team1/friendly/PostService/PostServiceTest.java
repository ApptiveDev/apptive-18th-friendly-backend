package apptive.team1.friendly.PostService;

import apptive.team1.friendly.domain.post.dto.PostFormDto;
import apptive.team1.friendly.domain.post.dto.PostListDto;
import apptive.team1.friendly.domain.post.entity.HashTag;
import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.post.repository.PostRepository;
import apptive.team1.friendly.domain.post.service.PostService;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
import apptive.team1.friendly.domain.user.service.UserService;
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
    @Autowired UserService userService;
    @Autowired AccountRepository accountRepository;

    @Test
    public void 게시물_추가() {
//        List<String> rules = new ArrayList<String>();
//        rules.add("rule1");
//        rules.add("rule2");
//        List<HashTag> hashTag = new ArrayList<HashTag>();
//        hashTag.add(LIFE);
//        hashTag.add(NATIVE);
//
//        PostFormDto newPostForm = new PostFormDto("title3", hashTag, 5,  "desc1", LocalDateTime.now(), "loc1", rules);
//
//        PostFormDto newPostForm2 = new PostFormDto("title44", hashTag, 3,  "des4", LocalDateTime.now(), "loc4", rules);
//
//        Long postId = postService.addPost(newPostForm);
//        postService.addPost(newPostForm2);
//
//        Assert.assertEquals(postId, postService.findByPostId(postId).getId());
    }

    @Test
    public void 유저로_게시물_조회() {
        List<Post> postsByUserId = postService.findPostsByUserId(2L);
        for(Post post: postsByUserId) {
            System.out.println("post.getId() + post.getTitle() = " + post.getId() + ' ' + post.getTitle());
        }
        Assert.assertEquals(2, postsByUserId.size());
    }
    
    @Test
    public void 게시물_리스트_조회() {
//        List<String> rules = new ArrayList<String>();
//        rules.add("rule1");
//        rules.add("rule2");
//        List<HashTag> hashTag = new ArrayList<HashTag>();
//        hashTag.add(LIFE);
//        hashTag.add(NATIVE);
//
//        Post newPost1 = new Post( "title1", "description1", 5, LocalDateTime.now(), "yangsan", rules, hashTag);
//        AccountPost accountPost = new AccountPost();
//        accountPost.setPost(newPost1);
//
//        Post newPost2 = new Post( "title2", "description2", 3, LocalDateTime.now(), "usan", rules, hashTag);
//        AccountPost accountPost2 = new AccountPost();
//        accountPost2.setPost(newPost2);
//
//        postService.addPost(newPost1);
//        postService.addPost(newPost2);

        List<PostListDto> postListDtos = postService.findAll();
        Assert.assertEquals(11, postListDtos.size());
    }

    @Test
    public void 게시물_조회() {
        List<String> rules = new ArrayList<String>();
        rules.add("rule1");
        rules.add("rule2");
        List<HashTag> hashTag = new ArrayList<HashTag>();
        hashTag.add(LIFE);
        hashTag.add(NATIVE);

        PostFormDto newPostForm = new PostFormDto("title3", hashTag, 5,  "desc1", LocalDateTime.now(), "loc1", rules);

        PostFormDto newPostForm2 = new PostFormDto("title5", hashTag, 3,  "desc11", LocalDateTime.now(), "12", rules);

        Long postId1 = postService.addPost(newPostForm);
        Long postId2 = postService.addPost(newPostForm2);

        Post findPost = postService.findByPostId(postId1);

        Assert.assertEquals(findPost, postId1);
    }
    @Test
    public void 게시물_삭제() {
        List<Post> postsByUserId = postService.findPostsByUserId(9L);
        Post post = postsByUserId.get(0);

        Long postId = postService.deletePost(post.getId());

        Assert.assertEquals(post.getId(), postId);
    }

    @Test
    public void 게시물_업데이트() {
        PostFormDto postFormDto = new PostFormDto();
        postFormDto.setTitle("modify");
        postFormDto.setDescription("updated!");
//        postFormDto.setPostId(4L);

        postService.updatePost(4L, postFormDto);

    }
}
