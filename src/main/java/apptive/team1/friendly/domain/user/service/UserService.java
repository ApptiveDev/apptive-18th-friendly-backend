package apptive.team1.friendly.domain.user.service;

import apptive.team1.friendly.domain.user.data.dto.GoogleSignUpRequest;
import apptive.team1.friendly.domain.user.data.dto.SignupRequest;
import apptive.team1.friendly.domain.user.data.dto.SignupResponse;
import apptive.team1.friendly.domain.user.data.dto.UserInfoResponse;
import apptive.team1.friendly.domain.user.data.dto.profile.EntityToDtoConverter;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.entity.AccountAuthority;
import apptive.team1.friendly.domain.user.data.entity.Authority;
import apptive.team1.friendly.domain.user.data.entity.profile.*;
import apptive.team1.friendly.domain.user.data.repository.*;
import apptive.team1.friendly.jwt.JwtTokenProvider;
import apptive.team1.friendly.utils.SecurityUtil;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final AccountRepository accountRepository;
    private final AuthorityRepository authorityRepository;
    private final AccountAuthorityRepository accountAuthorityRepository;
    private final InterestRepository interestRepository;
    private final LanguageRepository languageRepository;
    private final NationRepository nationRepository;
    private final AccountInterestRepository accountInterestRepository;
    private final AccountLanguageRepository accountLanguageRepository;
    private final AccountNationRepository accountNationRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(AccountRepository accountRepository, AuthorityRepository authorityRepository, AccountAuthorityRepository accountAuthorityRepository, InterestRepository interestRepository, LanguageRepository languageRepository, NationRepository nationRepository, AccountInterestRepository accountInterestRepository, AccountLanguageRepository accountLanguageRepository, AccountNationRepository accountNationRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.accountRepository = accountRepository;
        this.authorityRepository = authorityRepository;
        this.accountAuthorityRepository = accountAuthorityRepository;
        this.interestRepository = interestRepository;
        this.languageRepository = languageRepository;
        this.nationRepository = nationRepository;
        this.accountInterestRepository = accountInterestRepository;
        this.accountLanguageRepository = accountLanguageRepository;
        this.accountNationRepository = accountNationRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 회원가입
     */
    @Transactional
    public SignupResponse signUp(SignupRequest signupRequest) {
        if (accountRepository.findOneWithAccountAuthoritiesByEmail(signupRequest.getEmail()).orElseGet(() -> null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = authorityRepository.getReferenceById("ROLE_USER");

        Account user = Account.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .introduction(signupRequest.getIntroduction())
                .gender(signupRequest.getGender())
                .activated(true)
                .build();

        for (String interestName : signupRequest.getInterests()) {
            Interest interest = interestRepository.findOneByName(interestName).orElseGet(() -> {
                Interest newInterest = Interest.builder().name(interestName).build();
                return interestRepository.save(newInterest);
            });

            AccountInterest accountInterest = AccountInterest.builder()
                    .account(user)
                    .interest(interest)
                    .build();

            accountInterestRepository.save(accountInterest);
        }

        for (int i = 0; i < signupRequest.getLanguages().size(); i++) {
            String languageName = signupRequest.getLanguages().get(i);
            Language language = languageRepository.findOneByName(languageName).orElseGet(() -> {
                Language newLanguage = Language.builder().name(languageName).build();
                return languageRepository.save(newLanguage);
            });

            AccountLanguage accountLanguage = AccountLanguage.builder()
                    .account(user)
                    .language(language)
                    .level(signupRequest.getLanguageLevels().get(i))
                    .build();

            accountLanguageRepository.save(accountLanguage);
        }

        Nation nation = nationRepository.findOneByName(signupRequest.getNation()).orElseGet(() -> {
            Nation newNation = Nation.builder().name(signupRequest.getNation()).build();
            return nationRepository.save(newNation);
        });

        AccountNation accountNation = AccountNation.builder()
                .account(user)
                .nation(nation)
                .build();

        accountNationRepository.save(accountNation);

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
     * 구글 추가정보 회원가입
     * 이미 로그인에서 임시로 회원가입 해둔 회원을 찾아야함
     * 비밀번호 설정이 따로 필요없음
     */
    @Transactional
    public SignupResponse googleSignUp(GoogleSignUpRequest signupRequest, String token) {

        Authority authority = authorityRepository.getReferenceById("ROLE_USER");

        String email = jwtTokenProvider.getClaimsFromToken(token).getSubject();
        Account user = accountRepository.findOneWithAccountAuthoritiesByEmail(email).orElseThrow(() -> new RuntimeException("회원을 찾을 수가 없습니다."));

        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setIntroduction(signupRequest.getIntroduction());
        user.setGender(signupRequest.getGender());
        user.setActivated(true);

        for (String interestName : signupRequest.getInterests()) {
            Interest interest = interestRepository.findOneByName(interestName).orElseGet(() -> {
                Interest newInterest = Interest.builder().name(interestName).build();
                return interestRepository.save(newInterest);
            });

            AccountInterest accountInterest = AccountInterest.builder()
                    .account(user)
                    .interest(interest)
                    .build();

            accountInterestRepository.save(accountInterest);
        }

        for (int i = 0; i < signupRequest.getLanguages().size(); i++) {
            String languageName = signupRequest.getLanguages().get(i);
            Language language = languageRepository.findOneByName(languageName).orElseGet(() -> {
                Language newLanguage = Language.builder().name(languageName).build();
                return languageRepository.save(newLanguage);
            });

            AccountLanguage accountLanguage = AccountLanguage.builder()
                    .account(user)
                    .language(language)
                    .level(signupRequest.getLanguageLevels().get(i))
                    .build();

            accountLanguageRepository.save(accountLanguage);
        }

        Nation nation = nationRepository.findOneByName(signupRequest.getNation()).orElseGet(() -> {
            Nation newNation = Nation.builder().name(signupRequest.getNation()).build();
            return nationRepository.save(newNation);
        });

        AccountNation accountNation = AccountNation.builder()
                .account(user)
                .nation(nation)
                .build();

        accountNationRepository.save(accountNation);

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
     * Account 객체를 UserInfoResponse로 반환
     */
    public UserInfoResponse accountToUserInfo(Account account) {
        List<AccountInterest> accountInterests = accountInterestRepository.findAllByAccount(account);
        List<AccountLanguage> accountLanguages = accountLanguageRepository.findAllByAccount(account);
        AccountNation accountNation = accountNationRepository.findOneByAccount(account).orElseGet(() -> null);

        return UserInfoResponse.builder()
                .email(account.getEmail())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .gender(account.getGender())
                .introduction(account.getIntroduction())
                .interests(accountInterests.stream()
                        .map(EntityToDtoConverter::interestToInterestDto)
                        .collect(Collectors.toList()))
                .languages(accountLanguages.stream()
                        .map(EntityToDtoConverter::languageToLanguageDto)
                        .collect(Collectors.toList()))
                .nation(EntityToDtoConverter.nationToNationDto(accountNation))
                .accountAuthorities(account.getAccountAuthorities().stream()
                        .map(accountAuthority -> accountAuthority.getAuthority().getAuthorityName())
                        .collect(Collectors.toSet()))
                .build();
    }

    /**
     * 최근 로그인 사용자 정보 조회
     */
    @Transactional(readOnly = true)
    public UserInfoResponse getUserWithAuthorities() {
        return accountToUserInfo(SecurityUtil.getCurrentUserName().flatMap(accountRepository::findOneWithAccountAuthoritiesByEmail).orElseGet(() -> null));
    }

    /**
     * email로 UserInfoResponse 반환
     */
    @Transactional(readOnly = true)
    public UserInfoResponse getUserWithAuthoritiesByEmail(String email) {
        return accountToUserInfo(accountRepository.findOneWithAccountAuthoritiesByEmail(email).orElseGet(() -> null));
    }
}
