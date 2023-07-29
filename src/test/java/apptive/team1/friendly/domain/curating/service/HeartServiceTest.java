package apptive.team1.friendly.domain.curating.service;

import apptive.team1.friendly.domain.curating.dto.ContentFormDto;
import apptive.team1.friendly.domain.curating.exception.CanNotDeleteHeartException;
import apptive.team1.friendly.domain.curating.exception.CanNotPushHeartException;
import apptive.team1.friendly.domain.curating.repository.ContentRepository;
import apptive.team1.friendly.domain.curating.repository.HeartRepository;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class HeartServiceTest {

    @Autowired HeartService heartService;
    @Autowired HeartRepository heartRepository;
    @Autowired ContentRepository contentRepository;
    @Autowired ContentService contentService;
    @Autowired AccountRepository accountRepository;

    @Test
    public void 좋아요_누르기() throws Exception {
        //given
        Account account = createAccount("test@gmail.com", "kim", "mw");
        ContentFormDto contentForm = createContentForm("title1", "loc1", "11:00~13:00", "010-0000-0001", "instagram", "contents");
        List<MultipartFile> imageFiles = createImageFiles();
        Long contentId = contentService.addContent(account, contentForm, imageFiles);
        Account watcher = createAccount("watcher@gmail.com", "lee", "m");
        Account watcher2 = createAccount("watcher2@gmail.com", "kim", "m2");

        //when
        heartService.addHeart(watcher, contentId);
        heartService.addHeart(watcher2, contentId);

        //then
        Assertions.assertEquals(2, contentRepository.findOne(contentId).getHearts().size(), "좋아요를 누르면 하나 증가해야 한다.");
    }

    @Test(expected = CanNotPushHeartException.class)
    public void 본인글_좋아요_예외() throws Exception {
        //given
        Account account = createAccount("test@gmail.com", "kim", "mw");
        ContentFormDto contentForm = createContentForm("title1", "loc1", "11:00~13:00", "010-0000-0001", "instagram", "contents");
        List<MultipartFile> imageFiles = createImageFiles();
        Long contentId = contentService.addContent(account, contentForm, imageFiles);

        //when
        heartService.addHeart(account, contentId);

        //then
        fail("본인 글은 좋아요를 누를 수 없습니다.");
    }

    @Test(expected = CanNotPushHeartException.class)
    public void 같은글_좋아요_두번() throws Exception {
        //given
        Account account = createAccount("test@gmail.com", "kim", "mw");
        ContentFormDto contentForm = createContentForm("title1", "loc1", "11:00~13:00", "010-0000-0001", "instagram", "contents");
        List<MultipartFile> imageFiles = createImageFiles();
        Long contentId = contentService.addContent(account, contentForm, imageFiles);
        Account watcher = createAccount("watcher@gmail.com", "lee", "m");

        //when
        heartService.addHeart(watcher, contentId);
        heartService.addHeart(watcher, contentId);

        //then
        fail("하나의 글에는 한 번만 좋아요를 누를 수 있습니다.");
    }



    @Test
    public void 좋아요_취소() throws Exception {
        //given
        Account account = createAccount("test@gmail.com", "kim", "mw");
        ContentFormDto contentForm = createContentForm("title1", "loc1", "11:00~13:00", "010-0000-0001", "instagram", "contents");
        List<MultipartFile> imageFiles = createImageFiles();
        Long contentId = contentService.addContent(account, contentForm, imageFiles);
        Account watcher = createAccount("watcher@gmail.com", "lee", "m");
        heartService.addHeart(watcher, contentId);

        //when
        heartService.deleteHeart(watcher,contentId);

        //then
        Assertions.assertEquals(0, contentRepository.findOne(contentId).getHearts().size(), "좋아요를 취소하면 개수가 하나 감소해야 한다.");
    }

    @Test(expected = CanNotDeleteHeartException.class)
    public void 좋아요_누르기전_취소요청() throws Exception {
        //given
        Account account = createAccount("test@gmail.com", "kim", "mw");
        ContentFormDto contentForm = createContentForm("title1", "loc1", "11:00~13:00", "010-0000-0001", "instagram", "contents");
        List<MultipartFile> imageFiles = createImageFiles();
        Long contentId = contentService.addContent(account, contentForm, imageFiles);
        Account watcher = createAccount("watcher@gmail.com", "lee", "m");

        //when
        heartService.deleteHeart(watcher, contentId);

        //then
        fail("좋아요를 먼저 누르고 취소를 요청해야 합니다.");
    }


    private ContentFormDto createContentForm(String title, String location, String openingHours, String tel, String instagram, String content) {
        return new ContentFormDto(title, location, openingHours, tel, instagram, content);
    }

    private Account createAccount(String email, String firstName, String lastName) {
        Account account = new Account();
        account.setEmail(email);
        account.setFirstName(firstName);
        account.setLastName(lastName);
        accountRepository.save(account);
        return account;
    }

    private List<MultipartFile> createImageFiles() throws IOException {
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
        return files;
    }

}