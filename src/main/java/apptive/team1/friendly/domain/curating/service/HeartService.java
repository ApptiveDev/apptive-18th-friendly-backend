package apptive.team1.friendly.domain.curating.service;

import apptive.team1.friendly.domain.curating.entity.Content;
import apptive.team1.friendly.domain.curating.entity.Heart;
import apptive.team1.friendly.domain.curating.exception.CanNotDeleteHeartException;
import apptive.team1.friendly.domain.curating.exception.CanNotPushHeartException;
import apptive.team1.friendly.domain.curating.repository.ContentRepository;
import apptive.team1.friendly.domain.curating.repository.HeartRepository;
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
        Heart heartByUserIdAndContentId = heartRepository.findHeartByUserIdAndContentId(currentUser.getId(), contentId)
                .orElseThrow(() -> new CanNotPushHeartException("하나의 게시물에 한 번만 누를 수 있습니다."));

        // heart 생성
        Content content = contentRepository.findOne(contentId);
        Heart heart = Heart.create(currentUser, content);
        heartRepository.save(heart);
        
        return heart.getId();
    }

    @Transactional
    public Long deleteHeart(Account currentUser, Long contentId) {
        Heart heartByUserIdAndContentId = heartRepository.findHeartByUserIdAndContentId(currentUser.getId(), contentId)
                .orElseThrow(() -> new CanNotDeleteHeartException("좋아요를 먼저 눌러주세요."));

        heartRepository.delete(heartByUserIdAndContentId);

        return heartByUserIdAndContentId.getId();
    }
}
