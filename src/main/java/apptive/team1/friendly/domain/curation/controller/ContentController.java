package apptive.team1.friendly.domain.curation.controller;

import apptive.team1.friendly.domain.curation.dto.ContentDto;
import apptive.team1.friendly.domain.curation.dto.ContentFormDto;
import apptive.team1.friendly.domain.curation.dto.ContentListDto;
import apptive.team1.friendly.domain.curation.entity.SearchBase;
import apptive.team1.friendly.domain.curation.service.ContentService;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;
    private final UserService userService;

    /**
     *  큐레이션 리스트 조회
     */
    @GetMapping("/curation")
    public ResponseEntity<List<ContentListDto>> contentList(@RequestParam("base") String searchBase) {
        List<ContentListDto> contentListDtos = contentService.findAll(searchBase);
        return new ResponseEntity<>(contentListDtos, HttpStatus.OK);
    }

    /**
     * 큐레이션 상세 정보 조회
     */
    @GetMapping("/curation/{contentId}")
    public ResponseEntity<ContentDto> contentDetail(@PathVariable("contentId") Long contentId) {
        ContentDto contentDto = contentService.createContentDto(contentId);
        return new ResponseEntity<>(contentDto, HttpStatus.OK);
    }

    /**
     * 큐레이션 생성
     */
    @PostMapping("/curation")
    public ResponseEntity<Long> addContent(@RequestPart ContentFormDto formDto, @RequestPart List<MultipartFile> imageFiles) throws IOException {
        Account currentUser = userService.getCurrentUser();
        Long contentId = contentService.addContent(currentUser, formDto, imageFiles);
        return new ResponseEntity<>(contentId, HttpStatus.OK);
    }

    /**
     * 큐레이션 수정
     */
    @PutMapping("/curation/{contentId}/edit")
    public ResponseEntity<Long> editContent(@PathVariable("contentId") Long contentId,
                                            @RequestPart ContentFormDto updateFormDto, @RequestPart List<MultipartFile> imageFiles) throws IOException {
        Account currentUser = userService.getCurrentUser();
        Long updatedContentId = contentService.updateContent(currentUser, contentId, updateFormDto, imageFiles);
        return new ResponseEntity<>(updatedContentId, HttpStatus.OK);
    }

    /**
     * 큐레이션 삭제
     */
    @DeleteMapping("/curation/{contentId}")
    public ResponseEntity<Long> deleteContent(@PathVariable("contentId") Long contentId) {
        Account currentUser = userService.getCurrentUser();
        Long deletedContentId = contentService.deleteContent(currentUser, contentId);
        return new ResponseEntity<>(deletedContentId, HttpStatus.OK);
    }
}
