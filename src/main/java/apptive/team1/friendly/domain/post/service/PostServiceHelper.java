package apptive.team1.friendly.domain.post.service;

import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;

import java.util.Optional;

public final class PostServiceHelper {

    public static Account findExistingMember(AccountRepository accountRepository, Long accountId) {

        Optional<Account> authorOptional = accountRepository.findOneWithAccountAuthoritiesById(accountId);

        if(!authorOptional.isPresent()) throw new RuntimeException("존재하지 않는 user");

        return authorOptional.get();
    }
}
