package apptive.team1.friendly.domain.curation.service;

import apptive.team1.friendly.domain.curation.dto.ContentDto;
import apptive.team1.friendly.domain.curation.dto.ContentFormDto;
import apptive.team1.friendly.domain.curation.entity.Content;
import apptive.team1.friendly.domain.curation.entity.SearchBase;
import apptive.team1.friendly.domain.curation.repository.ContentRepository;
import apptive.team1.friendly.domain.post.exception.AccessDeniedException;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
import apptive.team1.friendly.global.TestMethods;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ContentServiceTest {
    @Autowired
    ContentService contentService;
    @Autowired
    ContentRepository contentRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    EntityManager em;
    @Autowired
    TestMethods tm;

    @Test
    public void 게시물_리스트_조회() throws Exception {
        //given
        Account account = tm.createAccount("TEST@gmail.com", "kim", "mw");
        ContentFormDto contentForm = createContentForm("test Title", "location", "11:00~12:00", "010-0000-0000", "instagram", "contents");
        ContentFormDto contentForm2 = createContentForm("test Title2", "location2", "11:00~14:00", "010-0000-0001", "instagram2", "contents2");
        List<MultipartFile> imageFiles = tm.createImageFiles();
        contentService.addContent(account, contentForm, imageFiles);
        contentService.addContent(account, contentForm2, imageFiles);

        //when
        List<Content> contents = contentRepository.findAll(SearchBase.LATEST);

        //then
        Assertions.assertEquals(2, contents.size(),"게시물을 추가한 만큼 개수가 늘어야 한다.");
    }

    @Test
    public void 게시물_정보_조회() throws Exception {
        //given
        Account account = tm.createAccount("TEST@gmail.com", "kim", "mw");
        ContentFormDto contentForm = createContentForm("test Title", "location", "11:00~12:00", "010-0000-0000", "instagram", "contents");
        List<MultipartFile> imageFiles = tm.createImageFiles();
        Long contentId = contentService.addContent(account, contentForm, imageFiles);

        //when
        ContentDto contentDto = contentService.createContentDto(contentId);

        //then
        Content content = contentRepository.findOne(contentId);

        Assertions.assertEquals(content.getId(), contentDto.getId(), "content로 생성한 contentDto의 id는 동일해야 한다.");
        Assertions.assertEquals(content.getTitle(), contentDto.getTitle(), "content로 생성한 contentDto의 title은 동일해야 한다.");
        Assertions.assertEquals(content.getTel(), contentDto.getTel(), "content로 생성한 contentDto의 tel은 동일해야 한다.");
        Assertions.assertEquals(content.getContent(), contentDto.getContent(), "content로 생성한 contentDto의 content는 동일해야 한다.");
        Assertions.assertEquals(content.getInstagram(), contentDto.getInstagram(), "content로 생성한 contentDto의 Instagram은 동일해야 한다.");
        Assertions.assertEquals(content.getLocation(), contentDto.getLocation(), "content로 생성한 contentDto의 Location은 동일해야 한다.");
        Assertions.assertEquals(content.getOpeningHours(), contentDto.getOpeningHours(), "content로 생성한 contentDto의 OpeningHours은 동일해야 한다.");
        Assertions.assertEquals(content.getAccount().getId(), contentDto.getAuthor().getId(), "content로 생성한 contentDto의 user는 동일해야 한다.");
    }

    @Test
    public void 게시물_생성() throws IOException {
        //given
        Account account = tm.createAccount("TEST@gmail.com", "kim", "mw");
        ContentFormDto contentForm = createContentForm("test Title", "location", "11:00~12:00", "010-0000-0000", "instagram", "contents");
        List<MultipartFile> imageFiles = tm.createImageFiles();

        //when
        Long contentId = contentService.addContent(account, contentForm, imageFiles);

        //then
        Assertions.assertEquals(contentId, contentRepository.findOne(contentId).getId(), "컨텐츠가 성공적으로 추가 되어야 한다.");
    }
    
    @Test
    public void 게시물_삭제() throws Exception {
        //given
        Account account = tm.createAccount("author@gmail.com", "kim", "mw");
        ContentFormDto contentForm = createContentForm("test", "loc", "1:00~2:00", "000-000-000", "instagram", "contents");
        List<MultipartFile> imageFiles = tm.createImageFiles();
        Long contentId = contentService.addContent(account, contentForm, imageFiles);

        //when
        Long deletedId = contentService.deleteContent(account, contentId);

        //then
        Assertions.assertEquals(contentId, deletedId, "삭제를 요청한 게시물이 삭제가 되어야 한다.");
        Assertions.assertEquals(contentRepository.findAll(SearchBase.LATEST).size(), 0, "게시물 삭제시 게시물 숫자가 감소해야 한다.");
    }

    @Test(expected = AccessDeniedException.class)
    public void 게시물_삭제_권한확인() throws Exception {
        //given
        Account account = tm.createAccount("author@gmail.com", "kim", "mw");
        ContentFormDto contentForm = createContentForm("test", "loc", "1:00~2:00", "000-000-000", "instagram", "contents");
        List<MultipartFile> imageFiles = tm.createImageFiles();
        Long contentId = contentService.addContent(account, contentForm, imageFiles);
        Account watcher = tm.createAccount("watcher@gmail.com", "lee", "mw");

        //when
        contentService.deleteContent(watcher, contentId);

        //then
        fail("게시물 작성자가 아닌 사람이 게시물을 삭제하려고 하면 예외가 발생해야 한다.");
    }

    @Test
    public void 게시물_업데이트() throws Exception {
        //given
        Account account = tm.createAccount("author@gmail.com", "kim", "mw");
        ContentFormDto contentForm = createContentForm("test", "loc", "1:00~2:00", "000-000-000", "instagram", "contents");
        List<MultipartFile> imageFiles = tm.createImageFiles();
        Long contentId = contentService.addContent(account, contentForm, imageFiles);

        //update info
        ContentFormDto updateForm = createContentForm("updated!", "loc2", "1:00~4:00", "000-000-001", "instagram2", "contents2");
        List<MultipartFile> updatedFiles = tm.createImageFiles();
        updatedFiles.remove(0);

        //when
        Long updatedId = contentService.updateContent(account, contentId, updateForm, updatedFiles);

        //then
        Content content = contentRepository.findOne(contentId);
        Assertions.assertEquals(contentId, updatedId,"원래 게시물과 수정한 게시물의 id는 동일하다");
        Assertions.assertEquals(updateForm.getTitle(),content.getTitle(), "업데이트가 성공적으로 수행되어야 한다.");
        Assertions.assertNotEquals(content.getImages().size(),contentRepository.findOne(updatedId).getImages(),
                "이미지 삭제 후 이미지 개수는 삭제 전과 달라야 한다");
    }


    @Test(expected = AccessDeniedException.class)
    public void 게시물_업데이트_권한확인() throws Exception {
        //given
        Account account = tm.createAccount("author@gmail.com", "kim", "mw");
        ContentFormDto contentForm = createContentForm("test", "loc", "1:00~2:00", "000-000-000", "instagram", "contents");
        List<MultipartFile> imageFiles = tm.createImageFiles();
        Long contentId = contentService.addContent(account, contentForm, imageFiles);
        Account watcher = tm.createAccount("watcher@gmail.com", "lee", "mw");

        //update info
        ContentFormDto updateForm = createContentForm("test", "loc", "1:00~2:00", "000-000-000", "instagram", "contents");
        List<MultipartFile> updatedFiles = tm.createImageFiles();

        //when
        contentService.updateContent(watcher, contentId, updateForm, updatedFiles);

        //then
        fail("게시물 작성자가 아닌 사람이 게시물을 삭제하려고 하면 예외가 발생해야 한다.");
    }


    private ContentFormDto createContentForm(String title, String location, String openingHours, String tel, String instagram, String content) {
        return new ContentFormDto(title, location, openingHours, tel, instagram, content);
    }

}