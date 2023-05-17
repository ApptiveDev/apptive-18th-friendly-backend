package apptive.team1.friendly.domain.user.service;

import apptive.team1.friendly.common.jwt.JwtTokenProvider;
import apptive.team1.friendly.common.s3.AwsS3Uploader;
import apptive.team1.friendly.common.s3.FileInfo;
import apptive.team1.friendly.domain.user.data.dto.AccountInfoResponse;
import apptive.team1.friendly.domain.user.data.dto.GoogleSignUpRequest;
import apptive.team1.friendly.domain.user.data.dto.SignupRequest;
import apptive.team1.friendly.domain.user.data.dto.SignupResponse;
import apptive.team1.friendly.domain.user.data.dto.profile.EntityToDtoConverter;
import apptive.team1.friendly.domain.user.data.dto.profile.ProfileImgDto;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.entity.AccountAuthority;
import apptive.team1.friendly.domain.user.data.entity.Authority;
import apptive.team1.friendly.domain.user.data.entity.profile.*;
import apptive.team1.friendly.domain.user.data.repository.*;
import apptive.team1.friendly.utils.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final AccountRepository accountRepository;
    private final AuthorityRepository authorityRepository;
    private final AccountAuthorityRepository accountAuthorityRepository;
    private final InterestRepository interestRepository;
    private final LanguageRepository languageRepository;
    private final LanguageLevelRepository languageLevelRepository;
    private final NationRepository nationRepository;
    private final GenderRepository genderRepository;
    private final AccountInterestRepository accountInterestRepository;
    private final AccountLanguageRepository accountLanguageRepository;
    private final AccountNationRepository accountNationRepository;
    private final ProfileImgRepository profileImgRepository;

    private final PasswordEncoder passwordEncoder;
    private final AwsS3Uploader awsS3Uploader;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(AccountRepository accountRepository, AuthorityRepository authorityRepository, AccountAuthorityRepository accountAuthorityRepository, InterestRepository interestRepository, LanguageRepository languageRepository, LanguageLevelRepository languageLevelRepository, NationRepository nationRepository, GenderRepository genderRepository, AccountInterestRepository accountInterestRepository, AccountLanguageRepository accountLanguageRepository, AccountNationRepository accountNationRepository, ProfileImgRepository profileImgRepository, AwsS3Uploader awsS3Uploader, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.accountRepository = accountRepository;
        this.authorityRepository = authorityRepository;
        this.accountAuthorityRepository = accountAuthorityRepository;
        this.interestRepository = interestRepository;
        this.languageRepository = languageRepository;
        this.languageLevelRepository = languageLevelRepository;
        this.nationRepository = nationRepository;
        this.genderRepository = genderRepository;
        this.accountInterestRepository = accountInterestRepository;
        this.accountLanguageRepository = accountLanguageRepository;
        this.accountNationRepository = accountNationRepository;
        this.profileImgRepository = profileImgRepository;
        this.awsS3Uploader = awsS3Uploader;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 회원가입
     */
    @Transactional
    public SignupResponse signUp(SignupRequest signupRequest) {
        if (accountRepository.findOneWithAccountAuthoritiesByEmail(signupRequest.getEmail()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = authorityRepository.getReferenceById("ROLE_USER");

        Account account = createAccount(signupRequest);

        addGender(account, signupRequest.getGender());
        addInterests(account, signupRequest.getInterests());
        addLanguages(account, signupRequest.getLanguages(), signupRequest.getLanguageLevels());
        addNation(account, signupRequest.getNation(), signupRequest.getCity());
        addAuthority(account, authority);

        return SignupResponse.of(accountRepository.save(account));
    }

    /**
     * 구글 추가정보 회원가입
     * 이미 로그인에서 임시로 회원가입 해둔 회원을 찾아야함
     * 비밀번호 설정이 따로 필요없음
     */
    @Transactional
    public SignupResponse googleSignUp(GoogleSignUpRequest signupRequest, String token) {

        String email = jwtTokenProvider.getClaimsFromToken(token).getSubject();
        Account account = accountRepository.findOneWithAccountAuthoritiesByEmail(email).orElseThrow(() -> new RuntimeException("구글 로그인 이후 추가 정보 회원가입이 가능합니다."));

        if (account.getFirstName() != null) {
            throw new RuntimeException("이미 추가 정보 회원가입되어 있는 회원입니다.");
        }

        extraSignup(account, signupRequest);

        addGender(account, signupRequest.getGender());
        addInterests(account, signupRequest.getInterests());
        addLanguages(account, signupRequest.getLanguages(), signupRequest.getLanguageLevels());
        addNation(account, signupRequest.getNation(), signupRequest.getCity());

        return SignupResponse.of(accountRepository.save(account));
    }

    private Account createAccount(SignupRequest signupRequest) {
        return Account.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .birthday(signupRequest.getBirthday())
                .introduction(signupRequest.getIntroduction())
                .activated(true)
                .build();
    }

    private void addInterests(Account account, List<String> interests) {
        interests.stream().forEach(interestName -> {
            Interest interest = interestRepository.findOneByName(interestName)
                    .orElseGet(() -> {
                        Interest newInterest = Interest.builder().name(interestName).build();
                        return interestRepository.save(newInterest);
                    });

            AccountInterest accountInterest = AccountInterest.builder()
                    .account(account)
                    .interest(interest)
                    .build();

            accountInterestRepository.save(accountInterest);
        });
    }

    private void addLanguages(Account user, List<String> languages, List<String> languageLevels) {
        for (int i = 0; i < languages.size(); i++) {
            String languageName = languages.get(i);
            Language language = languageRepository.findOneByName(languageName)
                    .orElseGet(() -> {
                        Language newLanguage = Language.builder().name(languageName).build();
                        return languageRepository.save(newLanguage);
                    });

            String languageLevelName = languageLevels.get(i);
            LanguageLevel languageLevel = languageLevelRepository.findOneByName(languageLevelName)
                    .orElseGet(() -> {
                        LanguageLevel newLevel = new LanguageLevel();
                        newLevel.setName(languageLevelName);
                        return languageLevelRepository.save(newLevel);
                    });

            AccountLanguage accountLanguage = AccountLanguage.builder()
                    .account(user)
                    .language(language)
                    .level(languageLevel)
                    .build();

            accountLanguageRepository.save(accountLanguage);
        }
    }

    private void addNation(Account user, String nationName, String cityName) {
        Nation nation = nationRepository.findOneByName(nationName)
                .orElseGet(() -> {
                    Nation newNation = Nation.builder().name(nationName).build();
                    return nationRepository.save(newNation);
                });

        AccountNation accountNation = AccountNation.builder()
                .account(user)
                .nation(nation)
                .city(cityName)
                .build();

        accountNationRepository.save(accountNation);
    }

    private void addGender(Account account, String genderName) {
        Gender gender = genderRepository.findOneByName(genderName)
                .orElseGet(() -> {
                    Gender newGender = new Gender();
                    newGender.setName(genderName);
                    return genderRepository.save(newGender);
                });

        account.setGender(gender);
    }

    private void addAuthority(Account user, Authority authority) {
        AccountAuthority accountAuthority = AccountAuthority.builder()
                .account(user)
                .authority(authority)
                .build();

        authority.getAccountAuthorities().add(accountAuthority);
        user.getAccountAuthorities().add(accountAuthority);

        accountAuthorityRepository.save(accountAuthority);
    }

    private void extraSignup(Account account, GoogleSignUpRequest signupRequest) {
        account.setBirthday(signupRequest.getBirthday());
        account.setFirstName(signupRequest.getFirstName());
        account.setLastName(signupRequest.getLastName());
        account.setIntroduction(signupRequest.getIntroduction());
        account.setActivated(true);
    }

    /**
     * Account 객체를 UserInfoResponse로 반환
     */
    public AccountInfoResponse accountToUserInfo(Account account) {
        List<AccountInterest> accountInterests = accountInterestRepository.findAllByAccount(account);
        List<AccountLanguage> accountLanguages = accountLanguageRepository.findAllByAccount(account);
        AccountNation accountNation = accountNationRepository.findOneByAccount(account).orElseGet(() -> null);

        return EntityToDtoConverter.accountToInfoDto(account, accountInterests, accountLanguages, accountNation);
    }

    /**
     * 최근 로그인 사용자 정보 조회
     */
    @Transactional(readOnly = true)
    public AccountInfoResponse getUserWithAuthorities() {
        return accountToUserInfo(SecurityUtil.getCurrentUserName().flatMap(accountRepository::findOneWithAccountAuthoritiesByEmail).orElseGet(() -> null));
    }

    /**
     * email로 UserInfoResponse 반환
     */
    @Transactional(readOnly = true)
    public AccountInfoResponse getUserWithAuthoritiesByEmail(String email) {
        return accountToUserInfo(accountRepository.findOneWithAccountAuthoritiesByEmail(email).orElseGet(() -> null));
    }

    /**
     * 회원 이미지 업로드
     */
    public ProfileImgDto accountProfileImgUpload(MultipartFile multipartFile) throws IOException {
        // 회원 찾기
        Account account = SecurityUtil.getCurrentUserName().flatMap(accountRepository::findOneWithAccountAuthoritiesByEmail).orElseGet(() -> null);
        if (account == null) throw new RuntimeException("회원을 찾을 수가 없습니다.");

        // 기존 이미지 제거
        Optional<ProfileImg> accountProfile = profileImgRepository.findOneByAccount(account);
        if (accountProfile.isPresent()) {
            awsS3Uploader.delete(accountProfile.get().getUploadFileName());
            profileImgRepository.delete(accountProfile.get());
        }

        // aws 파일 업로드
        FileInfo fileInfo = awsS3Uploader.upload(multipartFile, account.getEmail());
        ProfileImg profileImg = ProfileImg.of(account, fileInfo);

        // db에 이미지 정보 저장
        profileImgRepository.save(profileImg);

        // profileImg를 profileImgDto로 변환 후 반환
        return EntityToDtoConverter.profileImgToProfileImgDto(profileImg);
    }
}
