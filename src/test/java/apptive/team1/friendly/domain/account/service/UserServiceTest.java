package apptive.team1.friendly.domain.account.service;

import apptive.team1.friendly.domain.user.data.constant.LanguageLevel;
import apptive.team1.friendly.domain.user.data.dto.AccountInfoResponse;
import apptive.team1.friendly.domain.user.data.dto.SignupRequest;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.entity.Authority;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
import apptive.team1.friendly.domain.user.data.repository.AuthorityRepository;
import apptive.team1.friendly.domain.user.data.vo.Language;
import apptive.team1.friendly.domain.user.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AuthorityRepository authorityRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void 회원가입() throws Exception {
        //given
        Authority authority = authorityRepository.getReferenceById("ROLE_USER");
        Account account = getAccount(authority);

        //when
        accountRepository.save(account);

        //then
        Optional<Account> newAccount = accountRepository.findOneWithAccountAuthoritiesById(account.getId());
        Assertions.assertEquals( newAccount.get().getId(), account.getId(), "회원 가입이 성공적으로 수행되어야 한다.");
    }


    private static Account getAccount(Authority authority) {
        List<Language> languages = new ArrayList<>();
        List<String> interests = new ArrayList<>();
        languages.add(new Language("korean", LanguageLevel.ADVANCED));
        interests.add("축구");

        return Account.create("test@gmail.com", "abc", "kim", "mw",
                        "23-08-05", "남자", "intro", interests, "korea", "busan",
                        languages, authority);
    }
}
