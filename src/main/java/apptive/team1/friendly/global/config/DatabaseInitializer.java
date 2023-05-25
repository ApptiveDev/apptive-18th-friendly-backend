package apptive.team1.friendly.global.config;

import apptive.team1.friendly.domain.user.data.entity.Authority;
import apptive.team1.friendly.domain.user.data.repository.AccountAuthorityRepository;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
import apptive.team1.friendly.domain.user.data.repository.AuthorityRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements ApplicationRunner {

    private final AccountRepository accountRepository;
    private final AccountAuthorityRepository accountAuthorityRepository;
    private final AuthorityRepository authorityRepository;

    public DatabaseInitializer(AccountRepository accountRepository, AccountAuthorityRepository accountAuthorityRepository, AuthorityRepository authorityRepository) {
        this.accountRepository = accountRepository;
        this.accountAuthorityRepository = accountAuthorityRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Authority userAuthority = new Authority();
        userAuthority.setAuthorityName("ROLE_USER");

        Authority adminAuthority = new Authority();
        adminAuthority.setAuthorityName("ROLE_ADMIN");

        authorityRepository.save(userAuthority);
        authorityRepository.save(adminAuthority);
    }
}
