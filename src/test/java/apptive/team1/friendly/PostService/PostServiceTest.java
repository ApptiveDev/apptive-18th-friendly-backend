package apptive.team1.friendly.PostService;

import apptive.team1.friendly.domain.post.dto.PostDto;
import apptive.team1.friendly.domain.post.dto.PostFormDto;
import apptive.team1.friendly.domain.post.dto.PostListDto;
import apptive.team1.friendly.domain.post.entity.AccountPost;
import apptive.team1.friendly.domain.post.entity.HashTag;
import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.post.repository.AccountPostRepository;
import apptive.team1.friendly.domain.post.repository.PostRepository;
import apptive.team1.friendly.domain.post.service.PostService;
import apptive.team1.friendly.domain.post.vo.AudioGuide;
import apptive.team1.friendly.domain.user.data.dto.PostOwnerInfo;
import apptive.team1.friendly.domain.user.data.dto.SignupRequest;
import apptive.team1.friendly.domain.user.data.dto.SignupResponse;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.entity.profile.*;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
import apptive.team1.friendly.domain.user.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static apptive.team1.friendly.domain.post.entity.HashTag.LIFE;
import static apptive.team1.friendly.domain.post.entity.HashTag.NATIVE;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(value = true)
public class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserService userService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountPostRepository accountPostRepository;

    @Test
    public void 게시물_정상_추가() throws IOException {
        // given
        Account account = new Account();
        account.setEmail("TestAccount@gmail.com");
        account.setFirstName("KIM");
        account.setLastName("MW");
        Set<String> rules = new HashSet<>();
        rules.add("rule1");
        rules.add("rule2");
        Set<HashTag> hashTag = new HashSet<>();
        hashTag.add(LIFE);
        hashTag.add(NATIVE);
        List<MultipartFile> files = new ArrayList<>();
        MockMultipartFile file
                = new MockMultipartFile(
                "bus",
                "bus.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(new File("src/test/resources/bus.jpg"))
        );
        MockMultipartFile file2
                = new MockMultipartFile(
                "zidane",
                "zidane.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(new File("src/test/resources/zidane.jpg"))
        );
        files.add(file);
        files.add(file2);
        AudioGuide audioGuide = new AudioGuide();
        PostFormDto newPostForm = new PostFormDto("title1", hashTag, 5, "desc1", LocalDateTime.now(), "loc1", rules, audioGuide);
        PostFormDto newPostForm2 = new PostFormDto("title2", hashTag, 3, "desc2", LocalDateTime.now(), "loc4", rules, audioGuide);


        // when
        Long postId = postService.addPost(account, newPostForm, files);
        Long postId2 = postService.addPost(account, newPostForm2, files);

        // then
        Assert.assertEquals(postId, postService.findByPostId(postId).getId());
        Assert.assertEquals(postId2, postService.findByPostId(postId2).getId());
    }

    @Test
    public void 게시물_리스트_조회() throws IOException {
        // given
        Account account = new Account();
        account.setEmail("TestAccount@gmail.com");
        account.setFirstName("KIM");
        account.setLastName("MW");
        Set<String> rules = new HashSet<>();
        rules.add("rule1");
        rules.add("rule2");
        Set<HashTag> hashTags = new HashSet<>();
        hashTags.add(LIFE);
        hashTags.add(NATIVE);
        AudioGuide audioGuide = new AudioGuide();
        List<MultipartFile> files = new ArrayList<>();
        MockMultipartFile file
                = new MockMultipartFile(
                "bus",
                "bus.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(new File("src/test/resources/bus.jpg"))
        );
        MockMultipartFile file2
                = new MockMultipartFile(
                "zidane",
                "zidane.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(new File("src/test/resources/zidane.jpg"))
        );
        files.add(file);
        files.add(file2);
        PostFormDto postFormDto1 = new PostFormDto("title1", hashTags, 5,
                "description", LocalDateTime.now(), "Yangsan", rules, audioGuide);

        PostFormDto postFormDto2 = new PostFormDto("title1", hashTags, 3,
                "description2", LocalDateTime.now(), "Busan", rules, audioGuide);

        // when
        postService.addPost(account, postFormDto1, files);
        postService.addPost(account, postFormDto2, files);

        List<PostListDto> postListDtos = postService.findAll();
        Assert.assertEquals(2, postListDtos.size());
    }

    @Test
    public void 게시물_조회() throws IOException {
        // given
        Account account = new Account();
        Set<String> rules = new HashSet<>();
        rules.add("rule1");
        rules.add("rule2");
        Set<HashTag> hashTags = new HashSet<>();
        hashTags.add(LIFE);
        hashTags.add(NATIVE);
        AudioGuide audioGuide = new AudioGuide();
        List<MultipartFile> files = new ArrayList<>();
        MockMultipartFile file
                = new MockMultipartFile(
                "bus",
                "bus.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(new File("src/test/resources/bus.jpg"))
        );
        MockMultipartFile file2
                = new MockMultipartFile(
                "zidane",
                "zidane.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(new File("src/test/resources/zidane.jpg"))
        );
        files.add(file);
        files.add(file2);
        PostFormDto postFormDto = new PostFormDto("title1", hashTags, 5,
                "description", LocalDateTime.now(), "Yangsan", rules, audioGuide);


        // when
        Long postId = postService.addPost(account, postFormDto, files);
        PostOwnerInfo postOwnerInfo = userService.getPostOwnerInfo(postId);
        PostDto postDto = postService.postDetail(postId, postOwnerInfo);

        // then
        Assert.assertEquals(postDto.getPostId(), postId);
    }

    @Test
    public void 게시물_삭제() throws IOException {
        // given
        Account account = new Account();
        account.setEmail("TestAccount@naver.com");
        accountRepository.save(account);
        Set<String> rules = new HashSet<>();
        rules.add("rule1");
        rules.add("rule2");
        Set<HashTag> hashTags = new HashSet<>();
        hashTags.add(LIFE);
        hashTags.add(NATIVE);
        AudioGuide audioGuide = new AudioGuide();
        List<MultipartFile> files = new ArrayList<>();
        MockMultipartFile file
                = new MockMultipartFile(
                "bus",
                "bus.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(new File("src/test/resources/bus.jpg"))
        );
        MockMultipartFile file2
                = new MockMultipartFile(
                "zidane",
                "zidane.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(new File("src/test/resources/zidane.jpg"))
        );
        files.add(file);
        files.add(file2);
        PostFormDto postFormDto = new PostFormDto("title1", hashTags, 5,
                "description", LocalDateTime.now(), "Yangsan", rules, audioGuide);

        // when
        Long addPostId = postService.addPost(account, postFormDto, files);
        Long deletePostId = postService.deletePost(account, addPostId);

        // then
        Assert.assertEquals(addPostId, deletePostId);
    }

    @Test
    public void 게시물_업데이트() throws IOException {
        // given
        Account account = new Account();
        account.setEmail("TestAccount@naver.com");
        accountRepository.save(account);
        Set<String> rules = new HashSet<>();
        rules.add("rule1");
        rules.add("rule2");
        Set<HashTag> hashTags = new HashSet<>();
        hashTags.add(LIFE);
        hashTags.add(NATIVE);
        AudioGuide audioGuide = new AudioGuide();
        List<MultipartFile> files = new ArrayList<>();
        MockMultipartFile file
                = new MockMultipartFile(
                "bus",
                "bus.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(new File("src/test/resources/bus.jpg"))
        );
        MockMultipartFile file2
                = new MockMultipartFile(
                "zidane",
                "zidane.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(new File("src/test/resources/zidane.jpg"))
        );
        MockMultipartFile file3
                = new MockMultipartFile(
                "zidane",
                "zidane.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(new File("src/test/resources/zidane.jpg"))
        );
        files.add(file);
        files.add(file2);
        PostFormDto postFormDto = new PostFormDto("create!", hashTags, 5,
                "description", LocalDateTime.now(), "Yangsan", rules, audioGuide);
        PostFormDto updateFormDto = new PostFormDto("updated!!", hashTags, 5,
                "description", LocalDateTime.now(), "Yangsan", rules, audioGuide);


        // when
        Long postId = postService.addPost(account, postFormDto, files);
        Post post = postService.findByPostId(postId);
        String title = post.getTitle();

        files.add(file3);
        Long updatedPostId = postService.updatePost(account, postId, updateFormDto, files);
        Post updatedPost = postService.findByPostId(updatedPostId);

        // then
        Assert.assertEquals(postId, updatedPostId);
        Assert.assertNotEquals(title, updatedPost.getTitle());
        Assert.assertEquals("updated!!", updatedPost.getTitle());

    }


    @Test
    public void 유저_작성한_게시물_조회() throws IOException {
        // given
        Account account = new Account();
        account.setEmail("TestAccount@naver.com");
        accountRepository.save(account);
        Set<String> rules = new HashSet<>();
        rules.add("rule1");
        rules.add("rule2");
        Set<HashTag> hashTags = new HashSet<>();
        hashTags.add(LIFE);
        hashTags.add(NATIVE);
        AudioGuide audioGuide = new AudioGuide();
        List<MultipartFile> files = new ArrayList<>();
        MockMultipartFile file
                = new MockMultipartFile(
                "bus",
                "bus.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(new File("src/test/resources/bus.jpg"))
        );
        MockMultipartFile file2
                = new MockMultipartFile(
                "zidane",
                "zidane.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(new File("src/test/resources/zidane.jpg"))
        );
        MockMultipartFile file3
                = new MockMultipartFile(
                "zidane",
                "zidane.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(new File("src/test/resources/zidane.jpg"))
        );
        files.add(file);
        files.add(file2);
        PostFormDto postFormDto = new PostFormDto("create!", hashTags, 5,
                "description", LocalDateTime.now(), "Yangsan", rules, audioGuide);
        PostFormDto postFormDto2 = new PostFormDto("create2!", hashTags, 3,
                "description", LocalDateTime.now(), "Busan", rules, audioGuide);

        postService.addPost(account, postFormDto, files);
        postService.addPost(account, postFormDto2, files);

        List<Post> postsByUserEmail = postService.findPostsByUserEmail(account.getEmail());

        Assert.assertEquals(2, postsByUserEmail.size());
    }

//    @Test
//    public void 테스트용_회원추가() {
//        Account account = new Account();
//        AccountNation accountNation = new AccountNation();
//        accountNation.setAccount(account);
//        Nation nation = new Nation();
//        nation.setName("korea");
//        accountNation.setNation(nation);
//
//        ProfileImg profileImg = new ProfileImg();
//        profileImg.setUploadFileUrl("test");
//        profileImg.setAccount(account);
//
//        AccountLanguage accountLanguage1 = new AccountLanguage();
//        AccountLanguage accountLanguage2 = new AccountLanguage();
//        Language language1 = new Language();
//        Language language2 = new Language();
//        language1.setName("korean");
//        language2.setName("english");
//        accountLanguage1.setLanguage(language1);
//        accountLanguage2.setLanguage(language2);
//        accountLanguage1.setAccount(account);
//        accountLanguage2.setAccount(account);
//        accountRepository.save(account);
//
//    }
}