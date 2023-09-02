package apptive.team1.friendly.domain.post.service;

import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.post.repository.PostRepository;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static apptive.team1.friendly.domain.post.service.PostServiceHelper.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostJoinService {

    private final AccountRepository accountRepository;

    private final PostRepository postRepository;

    /**
     * 같이가요 참가 신청
     */
    @Transactional
    public void applyJoin(Long currentUserId, Long postId) {

        Account currentUser = findExistingMember(accountRepository, currentUserId);

        Post findPost = postRepository.findOneByPostId(postId);

        findPost.addParticipant(currentUser);
    }

    @Transactional
    public void cancelJoin(Long currentUserId, Long postId) {

        Account currentUser = findExistingMember(accountRepository, currentUserId);

        Post findPost = postRepository.findOneByPostId(postId);

        findPost.deleteParticipant(currentUser);
    }
}
