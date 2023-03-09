package apptive.team1.friendly.domain.user.service;

import apptive.team1.friendly.domain.user.data.dto.RequestSignUp;
import apptive.team1.friendly.domain.user.data.dto.ResponseSignUp;
import apptive.team1.friendly.domain.user.data.dto.ResponseUserInfo;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.entity.AccountAuthority;
import apptive.team1.friendly.domain.user.data.entity.Authority;
import apptive.team1.friendly.domain.user.data.repository.AccountAuthorityRepository;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
import apptive.team1.friendly.utils.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    private final AccountRepository accountRepository;
    private final AccountAuthorityRepository accountAuthorityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(AccountRepository accountRepository, AccountAuthorityRepository accountAuthorityRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.accountAuthorityRepository = accountAuthorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원가입
     */
    @Transactional
    public ResponseSignUp signUp(RequestSignUp requestSignUp) {
        if (accountRepository.findOneWithAccountAuthoritiesByUsername(requestSignUp.getUsername()).orElseGet(() -> null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Account user = Account.builder()
                .username(requestSignUp.getUsername())
                .email(requestSignUp.getEmail())
                .password(passwordEncoder.encode(requestSignUp.getPassword()))
                .firstName(requestSignUp.getFirstName())
                .lastName(requestSignUp.getLastName())
                .nation(requestSignUp.getNation())
                .language(requestSignUp.getLanguage())
                .interest(requestSignUp.getInterest())
                .favorite(requestSignUp.getFavorite())
                .introduction(requestSignUp.getIntroduction())
                .gender(requestSignUp.getGender())
                .activated(true)
                .build();

        AccountAuthority accountAuthority = AccountAuthority.builder()
                .account(user)
                .authority(authority)
                .build();

        authority.getAccountAuthorities().add(accountAuthority);
        user.getAccountAuthorities().add(accountAuthority);

        accountAuthorityRepository.save(accountAuthority);
        return ResponseSignUp.of(accountRepository.save(user));
    }


    /**
     * username으로 UserInfo Dto 반환
     */
    @Transactional(readOnly = true)
    public ResponseUserInfo getUserWithAuthorities(String username) {
        return ResponseUserInfo.of(accountRepository.findOneWithAccountAuthoritiesByUsername(username).orElseGet(() -> null));
    }

    @Transactional(readOnly = true)
    public ResponseUserInfo getUserWithAuthorities() {
        return ResponseUserInfo.of(SecurityUtil.getCurrentUserName().flatMap(accountRepository::findOneWithAccountAuthoritiesByUsername).orElseGet(() -> null));
    }
}
