package apptive.team1.friendly.domain.post.service;

import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.post.exception.NoAccountException;
import apptive.team1.friendly.domain.post.exception.NoPostException;
import apptive.team1.friendly.domain.post.repository.PostRepository;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;

import java.util.Optional;

public final class PostServiceHelper {

    public static Account findExistingMember(AccountRepository accountRepository, Long accountId) {

        Optional<Account> authorOptional = accountRepository.findOneWithAccountAuthoritiesById(accountId);

        if(!authorOptional.isPresent()) throw new NoAccountException("존재하지 않는 유저입니다.");

        return authorOptional.get();
    }

    public static Post findExistingPost(PostRepository postRepository, Long postId) {

        Optional<Post> postOptional = postRepository.findOneByPostId(postId);

        if(!postOptional.isPresent())   throw new NoPostException("존재하지 않는 게시물입니다.");

        return postOptional.get();
    }
}
