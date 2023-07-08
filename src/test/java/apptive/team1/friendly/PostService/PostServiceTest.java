//package apptive.team1.friendly.PostService;
//
//import apptive.team1.friendly.domain.post.dto.PostFormDto;
//import apptive.team1.friendly.domain.post.dto.PostListDto;
//import apptive.team1.friendly.domain.post.entity.HashTag;
//import apptive.team1.friendly.domain.post.entity.Post;
//import apptive.team1.friendly.domain.post.repository.AccountPostRepository;
//import apptive.team1.friendly.domain.post.repository.PostRepository;
//import apptive.team1.friendly.domain.post.service.PostService;
//import apptive.team1.friendly.domain.user.data.dto.SignupRequest;
//import apptive.team1.friendly.domain.user.data.dto.SignupResponse;
//import apptive.team1.friendly.domain.user.data.entity.Account;
//import apptive.team1.friendly.domain.user.data.entity.profile.*;
//import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
//import apptive.team1.friendly.domain.user.service.UserService;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import static apptive.team1.friendly.domain.post.entity.HashTag.LIFE;
//import static apptive.team1.friendly.domain.post.entity.HashTag.NATIVE;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Transactional
//@Rollback(value = true)
//public class PostServiceTest {
//
//    @Autowired
//    PostService postService;
//    @Autowired
//    PostRepository postRepository;
//    @Autowired
//    UserService userService;
//    @Autowired
//    AccountRepository accountRepository;
//    @Autowired
//    AccountPostRepository accountPostRepository;
//
//    @Test
//    public void 게시물_정상_추가() throws IOException {
//        Set<String> rules = new HashSet<>();
//        rules.add("rule1");
//        rules.add("rule2");
//        Set<HashTag> hashTag = new HashSet<>();
//        hashTag.add(LIFE);
//        hashTag.add(NATIVE);
//        List<MultipartFile> files = new ArrayList<>();
//        MockMultipartFile file
//                = new MockMultipartFile(
//                "bus",
//                "bus.jpeg",
//                MediaType.IMAGE_JPEG_VALUE,
//                new FileInputStream(new File("src/test/resources/bus.jpg"))
//        );
//        MockMultipartFile file2
//                = new MockMultipartFile(
//                "zidane",
//                "zidane.jpeg",
//                MediaType.IMAGE_JPEG_VALUE,
//                new FileInputStream(new File("src/test/resources/zidane.jpg"))
//        );
//        files.add(file);
//        files.add(file2);
//        PostFormDto newPostForm = new PostFormDto("title1", hashTag, 5, "desc1", LocalDateTime.now(), "loc1", rules);
//
//        PostFormDto newPostForm2 = new PostFormDto("title2", hashTag, 3, "desc2", LocalDateTime.now(), "loc4", rules);
//
//        Long postId = postService.addPost(newPostForm, files);
//        Long postId2 = postService.addPost(newPostForm2, files);
//
//        Assert.assertEquals(postId, postService.findByPostId(postId).getId());
//        Assert.assertEquals(postId2, postService.findByPostId(postId2).getId());
//    }
//
////    @Test
////    public void 게시물_리스트_조회() {
////        List<String> rules = new ArrayList<String>();
////        rules.add("rule1");
////        rules.add("rule2");
////        List<HashTag> hashTag = new ArrayList<HashTag>();
////        hashTag.add(LIFE);
////        hashTag.add(NATIVE);
////
////        Post newPost1 = new Post( "title1", "description1", 5, LocalDateTime.now(), "yangsan", rules, hashTag);
////        AccountPost accountPost = new AccountPost();
////        accountPost.setPost(newPost1);
////
////        Post newPost2 = new Post( "title2", "description2", 3, LocalDateTime.now(), "usan", rules, hashTag);
////        AccountPost accountPost2 = new AccountPost();
////        accountPost2.setPost(newPost2);
////
////        postService.addPost(newPost1);
////        postService.addPost(newPost2);
////
////        List<PostListDto> postListDtos = postService.findAll();
////        Assert.assertEquals(11, postListDtos.size());
////    }
//
////    @Test
////    public void 게시물_조회() {
////        Set<String> rules = new HashSet<>();
////        rules.add("rule1");
////        rules.add("rule2");
////        Set<HashTag> hashTag = new HashSet<>();
////        hashTag.add(LIFE);
////        hashTag.add(NATIVE);
////
////        PostFormDto newPostForm = new PostFormDto("title3", hashTag, 5,  "desc1", LocalDateTime.now(), "loc1", rules);
////
////        PostFormDto newPostForm2 = new PostFormDto("title5", hashTag, 3,  "desc11", LocalDateTime.now(), "12", rules);
////
////        Long postId1 = postService.addPost(newPostForm);
////        Long postId2 = postService.addPost(newPostForm2);
////
////        Post findPost = postService.findByPostId(postId1);
////
////        Assert.assertEquals(findPost, postId1);
////    }
////    @Test
////    public void 게시물_삭제() {
////        List<Post> postsByUserId = postService.findPostsByUserId(9L);
////        Post post = postsByUserId.get(0);
////
////        Long postId = postService.deletePost(post.getId());
////
////        Assert.assertEquals(post.getId(), postId);
////    }
//
////    @Test
////    public void 게시물_업데이트() {
////        PostFormDto postFormDto = new PostFormDto();
////        postFormDto.setTitle("modify");
////        postFormDto.setDescription("updated!");
//////        postFormDto.setPostId(4L);
////
////        postService.updatePost(4L, postFormDto);
////
////    }
//
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
//
//
////    @Test
////    public void 유저로_게시물_조회() {
////        // 회원에 필요한 정보 입력
////        SignupRequest signupRequest = new SignupRequest();
////        signupRequest.setEmail("abc@naver.com");
////        List<String> interests = new ArrayList<>();
////        interests.add("inter1");
////        List<String> languages = new ArrayList<>();
////        languages.add("lan1");
////        List<String> languageLevels= new ArrayList<>();
////        languageLevels.add("native");
////
////        // 회원 가입 신청
////        signupRequest.setInterests(interests);
////        signupRequest.setLanguages(languages);
////        signupRequest.setLanguageLevels(languageLevels);
////        SignupResponse signupResponse = userService.signUp(signupRequest);
////
////
////        // userEmail로 조회한 게시물 찾기
////        List<Post> postsByUserId = postService.findPostsByUserEmail(signupResponse.getEmail());
////
////        Assert.assertEquals(2, postsByUserId.size());
////    }
//}
//
//// 지금 코드가 현재 로그인된 유저를 기반으로 게시물을 추가, 삭제, 업데이트를 하고 있기 때문에
//// 테스트를 어떻게 해야할지 잘 모르겠음. 추후 공부해서 더 추가할 예정