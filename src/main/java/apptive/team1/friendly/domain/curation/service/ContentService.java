package apptive.team1.friendly.domain.curation.service;

import apptive.team1.friendly.domain.curation.dto.ContentDto;
import apptive.team1.friendly.domain.curation.dto.ContentFormDto;
import apptive.team1.friendly.domain.curation.dto.ContentListDto;
import apptive.team1.friendly.domain.curation.entity.Content;
import apptive.team1.friendly.domain.curation.entity.Heart;
import apptive.team1.friendly.domain.curation.entity.SearchBase;
import apptive.team1.friendly.domain.curation.repository.ContentRepository;
import apptive.team1.friendly.domain.curation.repository.HeartRepository;
import apptive.team1.friendly.domain.user.data.dto.UserInfo;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.global.common.s3.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;
    private final HeartRepository heartRepository;
    private final AwsS3Uploader awsS3Uploader;

    /**
     * 전체 조회
     */
    public List<ContentListDto> findAll(String searchBase) {
        List<Content> contents = contentRepository.findAll(SearchBase.valueOf(searchBase.toUpperCase()));
        return ContentListDto.create(contents);
    }

    /**
     * 단건 조회
     */
    public Content findOne(Long contentId) {
        return contentRepository.findOne(contentId);
    }

    /**
     * 유저에게 보여줄 Content DTO 생성
     */
    public ContentDto createContentDto(Account currentUser, UserInfo authorInfo, Long contentId) {
        Content content = contentRepository.findOne(contentId);

        // 현재 로그인된 유저가 게시물에 대해 누른 좋아요 확인
        Optional<Heart> heartByUserIdAndContentId = heartRepository.findHeartByUserIdAndContentId(currentUser.getId(), contentId);
        boolean isCurrentUserPushLike = heartByUserIdAndContentId.isPresent();

        return ContentDto.create(content.getId(), authorInfo, content.getTitle(), content.getImages(),
                content.getLocation(), content.getInstagram(),
                content.getOpeningHours(), content.getTel(),
                content.getContent(), content.getHearts().size(), isCurrentUserPushLike);
    }

    /**
     * 추가
     */
    @Transactional
    public Long addContent(Account currentUser, ContentFormDto formDto, List<MultipartFile> multipartFiles) throws IOException {
        Content content = Content.createContent(currentUser, formDto);
        contentRepository.save(content);

        content.uploadImages(multipartFiles, awsS3Uploader);

        return content.getId();
    }

    /**
     * 삭제
     */
    @Transactional
    public Long deleteContent(Account currentUser, Long contentId) {
        Content content = contentRepository.findOne(contentId);

        content.deleteImages(currentUser, awsS3Uploader);

        contentRepository.delete(content);

        return content.getId();
    }

    /**
     * 업데이트
     */
    @Transactional
    public Long updateContent(Account currentUser, Long contentId, ContentFormDto form, List<MultipartFile> multipartFiles) throws IOException {
        Content content = contentRepository.findOne(contentId);

        content.deleteImages(currentUser, awsS3Uploader);

        content.update(currentUser, form);

        content.uploadImages(multipartFiles, awsS3Uploader);

        return content.getId();
    }

}
