package apptive.team1.friendly.domain.post.service;

import apptive.team1.friendly.domain.post.dto.PostDto;
import apptive.team1.friendly.domain.post.dto.PostFormDto;
import apptive.team1.friendly.domain.post.dto.PostListDto;
import apptive.team1.friendly.domain.post.entity.AccountPost;
import apptive.team1.friendly.domain.post.entity.AccountType;
import apptive.team1.friendly.domain.post.entity.HashTag;
import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.post.exception.AccessDeniedException;
import apptive.team1.friendly.domain.post.exception.ExcessOfPeopleException;
import apptive.team1.friendly.domain.post.exception.NotParticipantException;
import apptive.team1.friendly.domain.post.repository.PostRepository;
import apptive.team1.friendly.domain.post.entity.AudioGuide;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
import apptive.team1.friendly.global.TestMethods;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static apptive.team1.friendly.domain.post.entity.HashTag.*;
import static org.springframework.test.util.AssertionErrors.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    EntityManager em;
    @Autowired
    TestMethods tm;

    @Test
    public void 게시물_추가() throws IOException {
        // given
        Account account = tm.createAccount("TestAccount@gmail.com", "KIM", "MW");
        List<MultipartFile> files = tm.createImageFiles();
        PostFormDto newPostForm = createPostForm("title", 5, "add test", "location");

        // when
        Long postId = postService.addPost(account.getId(), newPostForm, files);

        // then
        Assert.assertEquals("게시물 추가가 성공적으로 되어야 한다.", postId, postRepository.findOneByPostId(postId).getId());
    }

    @Test
    public void 게시물_전체_조회() throws IOException {
        // given
        Account account = tm.createAccount("TestAccount@gmail.com", "KIM", "MW");
        List<MultipartFile> files = tm.createImageFiles();
        PostFormDto postFormDto1 = createPostForm("title", 5, "add", "location");
        PostFormDto postFormDto2 = createPostForm("title", 5, "add", "location");
        postService.addPost(account.getId(), postFormDto1, files);
        postService.addPost(account.getId(), postFormDto2, files);

        // when
        List<PostListDto> postListDtos = postService.findAll(null, null);

        // then
        Assert.assertEquals("추가한 게시물만큼 개수가 늘어야 한다.", 2, postListDtos.size());
    }

    @Test
    public void 게시물_해시태그_조회() throws Exception {
        //given
        Account account = tm.createAccount("TestAccount@gmail.com", "KIM", "MW");
        List<MultipartFile> files = tm.createImageFiles();
        PostFormDto postFormDto1 = createPostForm("title", 5, "add", "location");
        PostFormDto postFormDto2 = createUpdatePostForm();
        postService.addPost(account.getId(), postFormDto1, files);
        postService.addPost(account.getId(), postFormDto2, files);

        //when
        List<PostListDto> postListDtos = postService.findAll("famous", null);

        //then
        Assert.assertEquals("추가한 게시물 중 해시태그로 필터해서 검색한다.", 1, postListDtos.size());
    }

    @Test
    public void 게시물_키워드_조회() throws Exception {
        //given
        Account account = tm.createAccount("TestAccount@gmail.com", "KIM", "MW");
        List<MultipartFile> files = tm.createImageFiles();
        PostFormDto postFormDto1 = createPostForm("title", 5, "add", "location");
        PostFormDto postFormDto2 = createUpdatePostForm();
        PostFormDto postFormDto3 = createUpdatePostForm();
        postService.addPost(account.getId(), postFormDto1, files);
        postService.addPost(account.getId(), postFormDto2, files);
        postService.addPost(account.getId(), postFormDto3, files);

        //when
        List<PostListDto> postListDtos = postService.findAll(null, "updated");

        //then
        Assert.assertEquals("추가한 게시물 중 keyword로 필터해서 검색한다.", 2, postListDtos.size());
    }

    @Test
    public void 게시물_키워드_해시태그_조회() throws Exception {
        //given
        Account account = tm.createAccount("TestAccount@gmail.com", "KIM", "MW");
        List<MultipartFile> files = tm.createImageFiles();
        PostFormDto postFormDto1 = createPostForm("title", 5, "add", "location");
        PostFormDto postFormDto2 = createUpdatePostForm();
        PostFormDto postFormDto3 = createUpdatePostForm();
        postService.addPost(account.getId(), postFormDto1, files);
        postService.addPost(account.getId(), postFormDto2, files);
        postService.addPost(account.getId(), postFormDto3, files);

        //when
        List<PostListDto> postListDtos = postService.findAll("life", "updated");

        //then
        Assert.assertEquals("추가한 게시물 중 keyword와 hashtag로 필터해서 검색한다.", 2, postListDtos.size());
    }

    @Test
    public void 게시물_상세_조회() throws IOException {
        // given
        Account author = tm.createAccount("TestAccount@gmail.com", "KIM", "MW");

        Account currentUser = tm.createAccount("current@gmail.com", "Kim", "A");

        List<MultipartFile> files = tm.createImageFiles();
        PostFormDto postFormDto = createPostForm("title", 5, "add", "location");
        Long postId = postService.addPost(author.getId(), postFormDto, files);

        // when
        PostDto postDto = postService.postDetail(postId, currentUser.getId(), author.getId());

        // then
        Post findPost = postRepository.findOneByPostId(postId);
        Assert.assertEquals("게시물 id와 조회한 id가 일치해야 한다.", postId, postDto.getPostId());
        Assert.assertEquals("게시물 title과 조회한 title이 일치해야 한다.", findPost.getTitle(), postDto.getTitle() );
    }

    @Test
    public void 게시물_삭제() throws IOException {
        // given
        Account account = tm.createAccount("TestAccount@gmail.com", "KIM", "MW");
        List<MultipartFile> files = tm.createImageFiles();
        PostFormDto postFormDto = createPostForm("title", 5, "add", "location");
        Long addPostId = postService.addPost(account.getId(), postFormDto, files);

        // when
        Long deletePostId = postService.deletePost(account.getId(), addPostId);

        // then
        Assert.assertEquals("추가한 게시물과 삭제한 게시물의 아이디가 같아야 한다.", addPostId, deletePostId);
        Assert.assertEquals("삭제 후 게시물 개수는 줄어들어야 한다.", 0, postRepository.findAll(null, null).size());
    }

    @Test
    public void 게시물_업데이트() throws IOException {
        // given
        Account account = tm.createAccount("TestAccount@gmail.com", "KIM", "MW");
        List<MultipartFile> files = tm.createImageFiles();
        PostFormDto postFormDto = createPostForm("title", 5, "add", "location");
        PostFormDto updateFormDto = createUpdatePostForm();
        MockMultipartFile file
                = new MockMultipartFile(
                "zidane",
                "zidane.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(new File("src/test/resources/zidane.jpg"))
        );
        Long postId = postService.addPost(account.getId(), postFormDto, files);

        // when
        files.add(file);
        Post post = postRepository.findOneByPostId(postId);
        String title = post.getTitle();
        Long updatedPostId = postService.updatePost(account.getId(), postId, updateFormDto, files);
        Post updatedPost = postRepository.findOneByPostId(updatedPostId);

        // then
        Assert.assertEquals("업데이트 전과 후의 게시물 아이디는 동일하다",postId, updatedPostId);
        Assert.assertNotEquals("title을 업데이트하면 변경이 되어야 한다", title, updatedPost.getTitle());
        Assert.assertEquals("updated!!로 업데이트가 성공적으로 되었는지 확인", "updated!!", updatedPost.getTitle());
        Assert.assertEquals("이미지 추가가 성공적으로 되어야 한다.", 3, updatedPost.getPostImages().size());
    }
    @Test(expected = AccessDeniedException.class)
    public void 게시물_삭제_권한_테스트() throws Exception {
        //given
        Account author = tm.createAccount("ABC@gmail.com", "A", "BC");
        Account account = tm.createAccount("CD@gamil.com", "C", "D");
        PostFormDto postForm = createPostForm("text", 3, "설명", "장소");
        List<MultipartFile> imageFiles = tm.createImageFiles();
        Long postId = postService.addPost(author.getId(), postForm, imageFiles);

        //when
        postService.deletePost(account.getId(), postId);

        //then
        fail("게시물 작성자가 아닌 계정이 삭제시 예외가 발생해야 한다.");
    }

    @Test(expected = AccessDeniedException.class)
    public void 게시물_수정_권한_테스트() throws Exception {
        //given
        Account author = tm.createAccount("ABC@gmail.com", "A", "BC");
        Account account = tm.createAccount("CD@gamil.com", "C", "D");
        PostFormDto postForm = createPostForm("text", 3, "설명", "장소");
        List<MultipartFile> imageFiles = tm.createImageFiles();
        Long postId = postService.addPost(author.getId(), postForm, imageFiles);

        //when
        postService.updatePost(account.getId(), postId, postForm, imageFiles);

        //then
        fail("게시물 작성자가 아닌 계정이 삭제시 예외가 발생해야 한다.");
    }

    @Test
    public void 사진_삭제_테스트() throws IOException {
        // given
        Account account = tm.createAccount("TestAccount@gmail.com", "KIM", "MW");
        List<MultipartFile> files = tm.createImageFiles();
        PostFormDto postFormDto = createPostForm("title", 5, "add test", "location");
        PostFormDto updateFormDto = createUpdatePostForm();
        Long postId = postService.addPost(account.getId(), postFormDto, files);

        // when
        Post post = postService.findByPostId(postId);
        int imgCount = post.getPostImages().size();
        files.remove(0);
        postService.updatePost(account.getId(), postId, updateFormDto, files);

        // then
        Assert.assertNotEquals("삭제 전 이미지 개수와 삭제 후 개수는 달라야 한다.", imgCount, post.getPostImages().size());
        Assert.assertEquals("삭제 후 이미지 개수는 삭제한 이미지 개수만큼 줄어들어야 한다.", 1, post.getPostImages().size());
    }

    @Test
    public void 유저_작성한_게시물_조회() throws IOException {
        // given
        Account account = tm.createAccount("TestAccount@gmail.com", "KIM", "MW");
        List<MultipartFile> files = tm.createImageFiles();
        PostFormDto postFormDto = createPostForm("title", 5, "add", "location");
        PostFormDto postFormDto2 = createPostForm("title2", 3, "add2", "location2");

        postService.addPost(account.getId(), postFormDto, files);
        postService.addPost(account.getId(), postFormDto2, files);

        // when
        List<Post> postsByUserId = postService.findPostsByUserId(account.getId());

        // then
        Assert.assertEquals("유저가 작성한 게시물 수만큼 조회가 되어야 한다.",2, postsByUserId.size());
    }

    @Test
    public void 같이가요_참가신청_테스트() throws Exception {
        //given
        Account postOwner = tm.createAccount("TestAccount@gmail.com", "KIM", "MW");
        List<MultipartFile> files = tm.createImageFiles();
        PostFormDto postFormDto = createPostForm("title", 5, "add", "location");
        Long postId = postService.addPost(postOwner.getId(), postFormDto, files);

        Account participant = tm.createAccount("participant@gmail.com","A" , "B");

        //when
        postService.applyJoin(participant.getId(), postId);

        //then
        Post post = postRepository.findOneByPostId(postId);
        List<AccountPost> accountPosts = post.getAccountPosts();
        Assert.assertEquals("한 명이 참가신청을 하면 참가 인원은 2명이 되어야 한다.", 2, accountPosts.size());
        Assert.assertEquals("신청한 사람은 참여자가 되어야 한다.", AccountType.PARTICIPANT, accountPosts.get(accountPosts.size()-1).getAccountType());
        Assert.assertEquals("첫 번째는 게시물 작성자가 되어야 한다.", AccountType.AUTHOR, accountPosts.get(0).getAccountType());
    }

    @Test(expected = ExcessOfPeopleException.class)
    public void 인원초과_테스트() throws Exception {
        //given
        // 게시물 생성
        Account postOwner = tm.createAccount("TestAccount@gmail.com", "KIM", "MW");
        PostFormDto postFormDto = createPostForm("title", 2, "add", "location");
        List<MultipartFile> files = tm.createImageFiles();
        Long postId = postService.addPost(postOwner.getId(), postFormDto, files);

        // 참여자 목록
        Account participant = tm.createAccount("participant@gmail.com","A" , "B");
        Account participant2 = tm.createAccount("participant2@gmail.com","C" , "D");

        //when
        // 참여 신청
        postService.applyJoin(participant.getId(), postId);
        postService.applyJoin(participant2.getId(), postId);

        //then
        fail("인원 초과시 예외가 발생해야 한다.");
    }

    @Test
    public void 같이가요_참가취소_테스트() throws Exception {
        //given
        // 게시물 추가
        Account postOwner = tm.createAccount("TestAccount@gmail.com", "KIM", "MW");
        List<MultipartFile> files = tm.createImageFiles();
        PostFormDto postFormDto = createPostForm("title", 5, "add", "location");
        Long postId = postService.addPost(postOwner.getId(), postFormDto, files);

        // 참여 신청
        Account participant = tm.createAccount("participant@gmail.com","A" , "B");
        postService.applyJoin(participant.getId(), postId);

        //when
        // 참여 취소
        postService.cancelJoin(participant.getId(), postId);

        //then
        Post post = postRepository.findOneByPostId(postId);
        List<AccountPost> accountPosts = post.getAccountPosts();
        Assertions.assertEquals(1,accountPosts.size(), "참여 취소 시 참여 인원이 감소해야 한다.");
    }

    @Test(expected = NotParticipantException.class)
    public void 취소_예외() throws Exception {
        //given
        // 게시물 생성
        Account postOwner = tm.createAccount("TestAccount@gmail.com", "KIM", "MW");
        PostFormDto postFormDto = createPostForm("title", 2, "add", "location");
        List<MultipartFile> files = tm.createImageFiles();
        Long postId = postService.addPost(postOwner.getId(), postFormDto, files);

        // 참여자 목록
        Account participant = tm.createAccount("participant@gmail.com","A" , "B");
        Account participant2 = tm.createAccount("participant2@gmail.com","C" , "D");

        // 참여 신청
        postService.applyJoin(participant.getId(), postId);

        //when
        postService.cancelJoin(participant2.getId(), postId);

        //then
        fail("참여자가 아닌 이용자는 참가 취소를 할 수 없다.");
    }


    private PostFormDto createPostForm(String title, int maxPeople, String description, String location) {
        Set<String> rules = new HashSet<>();
        rules.add("rule1");
        rules.add("rule2");
        Set<HashTag> hashTag = new HashSet<>();
        hashTag.add(LIFE);
        hashTag.add(NATIVE);
        return new PostFormDto(title, hashTag, maxPeople, description, LocalDate.now(), LocalDate.of(2023,8,30), location, rules);
    }

    private PostFormDto createUpdatePostForm() {
        Set<String> rules = new HashSet<>();
        rules.add("rule1");
        Set<HashTag> hashTag = new HashSet<>();
        hashTag.add(LIFE);
        hashTag.add(NATIVE);
        hashTag.add(FAMOUS);
        return new PostFormDto("updated!!", hashTag, 5, "update test", LocalDate.now(), LocalDate.now(), "updated location", rules);
    }
}