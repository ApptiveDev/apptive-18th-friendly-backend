package apptive.team1.friendly.domain.curation.service;

import apptive.team1.friendly.domain.curation.entity.Content;
import apptive.team1.friendly.domain.curation.entity.Heart;
import apptive.team1.friendly.domain.curation.exception.CanNotDeleteHeartException;
import apptive.team1.friendly.domain.curation.exception.CanNotPushHeartException;
import apptive.team1.friendly.domain.curation.repository.ContentRepository;
import apptive.team1.friendly.domain.curation.repository.HeartRepository;
import apptive.team1.friendly.domain.user.data.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HeartService {

    private final HeartRepository heartRepository;
    private final ContentRepository contentRepository;

    @Transactional
    public Long addHeart(Account currentUser, Long contentId) {
        Content content = contentRepository.findOne(contentId);

        isAuthor(currentUser, content);

        Optional<Heart> heartByUserIdAndContentId = heartRepository.findHeartByUserIdAndContentId(currentUser.getId(), contentId);
        if(heartByUserIdAndContentId.isPresent()) {
            throw new CanNotPushHeartException("하나의 게시물에 하나만 누를 수 있습니다.");
        }

        // heart 생성
        Heart heart = Heart.create(currentUser, content);
        heartRepository.save(heart);
        
        return heart.getId();
    }

    private void isAuthor(Account currentUser, Content content) {
        if(content.getAccount() != null && currentUser.getId() == content.getAccount().getId()) {
            throw new CanNotPushHeartException("자신의 글에는 좋아요를 누를 수 없습니다.");
        }
    }

    @Transactional
    public Long deleteHeart(Account currentUser, Long contentId) {
        Heart heartByUserIdAndContentId = heartRepository.findHeartByUserIdAndContentId(currentUser.getId(), contentId)
                .orElseThrow(() -> new CanNotDeleteHeartException("좋아요를 먼저 눌러주세요."));

        Content content = contentRepository.findOne(contentId);

        content.getHearts().remove(heartByUserIdAndContentId);

        heartRepository.delete(heartByUserIdAndContentId);

        return heartByUserIdAndContentId.getId();
    }
}
