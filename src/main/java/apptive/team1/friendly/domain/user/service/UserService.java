package apptive.team1.friendly.domain.user.service;

import apptive.team1.friendly.domain.user.data.dto.SignupRequest;
import apptive.team1.friendly.domain.user.data.dto.SignupResponse;
import apptive.team1.friendly.domain.user.data.dto.UserInfoResponse;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.entity.AccountAuthority;
import apptive.team1.friendly.domain.user.data.entity.Authority;
import apptive.team1.friendly.domain.user.data.repository.AccountAuthorityRepository;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
import apptive.team1.friendly.utils.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public SignupResponse signUp(SignupRequest signupRequest) {
        if (accountRepository.findOneWithAccountAuthoritiesByUsername(signupRequest.getUsername()).orElseGet(() -> null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Account user = Account.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .nation(signupRequest.getNation())
                .language(signupRequest.getLanguage())
                .interest(signupRequest.getInterest())
                .favorite(signupRequest.getFavorite())
                .introduction(signupRequest.getIntroduction())
                .gender(signupRequest.getGender())
                .activated(true)
                .build();

        AccountAuthority accountAuthority = AccountAuthority.builder()
                .account(user)
                .authority(authority)
                .build();

        authority.getAccountAuthorities().add(accountAuthority);
        user.getAccountAuthorities().add(accountAuthority);

        accountAuthorityRepository.save(accountAuthority);
        return SignupResponse.of(accountRepository.save(user));
    }


    /**
     * username으로 UserInfo Dto 반환
     */
    @Transactional(readOnly = true)
    public UserInfoResponse getUserWithAuthorities(String username) {
        return UserInfoResponse.of(accountRepository.findOneWithAccountAuthoritiesByUsername(username).orElseGet(() -> null));
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserWithAuthorities() {
        return UserInfoResponse.of(SecurityUtil.getCurrentUserName().flatMap(accountRepository::findOneWithAccountAuthoritiesByUsername).orElseGet(() -> null));
    }
}
