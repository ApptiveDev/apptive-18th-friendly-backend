package apptive.team1.friendly.global.config;

import apptive.team1.friendly.domain.user.data.entity.Authority;
import apptive.team1.friendly.domain.user.data.repository.AuthorityRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements ApplicationRunner {

    private final AuthorityRepository authorityRepository;

    public DatabaseInitializer(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Authority userAuthority = new Authority("ROLE_USER");

        Authority adminAuthority = new Authority("ROLE_ADMIN");

        authorityRepository.save(userAuthority);
        authorityRepository.save(adminAuthority);
    }
}
