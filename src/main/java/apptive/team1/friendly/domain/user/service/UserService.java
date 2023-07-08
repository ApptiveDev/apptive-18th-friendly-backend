package apptive.team1.friendly.domain.user.service;

import apptive.team1.friendly.domain.post.entity.AccountPost;
import apptive.team1.friendly.domain.post.repository.AccountPostRepository;
import apptive.team1.friendly.domain.user.data.dto.*;
import apptive.team1.friendly.domain.user.data.dto.profile.LanguageDto;
import apptive.team1.friendly.domain.user.data.dto.profile.NationDto;
import apptive.team1.friendly.global.common.jwt.JwtTokenProvider;
import apptive.team1.friendly.global.common.s3.AwsS3Uploader;
import apptive.team1.friendly.global.common.s3.FileInfo;
import apptive.team1.friendly.domain.post.repository.AccountPostRepository;
import apptive.team1.friendly.domain.user.data.constant.LanguageLevel;
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
import apptive.team1.friendly.global.common.jwt.JwtTokenProvider;
import apptive.team1.friendly.global.common.s3.AwsS3Uploader;
import apptive.team1.friendly.global.common.s3.FileInfo;
import apptive.team1.friendly.global.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final AccountRepository accountRepository;
    private final AuthorityRepository authorityRepository;
    private final AccountAuthorityRepository accountAuthorityRepository;
    private final InterestRepository interestRepository;
    private final LanguageRepository languageRepository;
    private final LanguageLevelRepository languageLevelRepository;
    private final NationRepository nationRepository;
    private final AccountInterestRepository accountInterestRepository;
    private final AccountLanguageRepository accountLanguageRepository;
    private final AccountNationRepository accountNationRepository;
    private final AccountProfileImgRepository accountProfileImgRepository;
    private final AccountPostRepository accountPostRepository;

    private final PasswordEncoder passwordEncoder;
    private final AwsS3Uploader awsS3Uploader;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     */
    @Transactional
    public SignupResponse signUp(SignupRequest signupRequest) {
        Authority authority = authorityRepository.getReferenceById("ROLE_USER");

        Account account = createAccount(signupRequest);

        addInterests(account, signupRequest.getInterests());
        addLanguages(account, signupRequest.getLanguages(), signupRequest.getLanguageLevels());
        addNation(account, signupRequest.getNation(), signupRequest.getCity());
        addAuthority(account, authority);

        return SignupResponse.of(accountRepository.save(account));
    }

    /**
     * 추가정보 회원가입
     * 이미 로그인에서 임시로 회원가입 해둔 회원을 찾아야함
     * 비밀번호 설정이 따로 필요없음
     */
    @Transactional
    public SignupResponse extraSignUp(GoogleSignUpRequest signupRequest) {

        Account account = SecurityUtil.getCurrentUserName().flatMap(accountRepository::findOneWithAccountAuthoritiesByEmail).orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        extraSignup(account, signupRequest);

        addInterests(account, signupRequest.getInterests());
        addLanguages(account, signupRequest.getLanguages(), signupRequest.getLanguageLevels());
        addNation(account, signupRequest.getNation(), signupRequest.getCity());

        return SignupResponse.of(accountRepository.save(account));
    }

    private Account createAccount(SignupRequest signupRequest) {
        Account account = accountRepository.findOneByEmail(signupRequest.getEmail()).orElseGet(() -> accountRepository.save(new Account()));

        account.setGender(signupRequest.getGender());
        account.setEmail(signupRequest.getEmail());
        account.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        account.setFirstName(signupRequest.getFirstName());
        account.setLastName(signupRequest.getLastName());
        account.setBirthday(signupRequest.getBirthday());
        account.setIntroduction(signupRequest.getIntroduction());
        account.setActivated(true);

        return account;
    }

    private void addInterests(Account account, List<String> interests) {
        accountInterestRepository.deleteByAccount(account);
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

    private void addLanguages(Account account, List<String> languages, List<String> languageLevels) {
        accountLanguageRepository.deleteByAccount(account);

        for (int i = 0; i < languages.size(); i++) {
            String languageName = languages.get(i);
            Language language = languageRepository.findOneByName(languageName)
                    .orElseGet(() -> {
                        Language newLanguage = Language.builder().name(languageName).build();
                        return languageRepository.save(newLanguage);
                    });

            String languageLevelName = languageLevels.get(i);
            LanguageLevel languageLevel = LanguageLevel.getLevelByName(languageLevelName);

            AccountLanguage accountLanguage = AccountLanguage.builder()
                    .account(account)
                    .language(language)
                    .languageLevel(languageLevel)
                    .build();

            accountLanguageRepository.save(accountLanguage);
        }
    }

    private void addNation(Account account, String nationName, String cityName) {
        Nation nation = nationRepository.findOneByName(nationName)
                .orElseGet(() -> {
                    Nation newNation = Nation.builder().name(nationName).build();
                    return nationRepository.save(newNation);
                });

        AccountNation accountNation = AccountNation.builder()
                .account(account)
                .nation(nation)
                .city(cityName)
                .build();

        accountNationRepository.deleteByAccount(account);
        accountNationRepository.save(accountNation);
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
        account.setGender(signupRequest.getGender());
        account.setActivated(true);
    }

    /**
     * Account 객체를 UserInfoResponse로 반환
     */
    public AccountInfoResponse accountToUserInfo(Account account) {
        List<AccountInterest> accountInterests = accountInterestRepository.findAllByAccount(account);
        List<AccountLanguage> accountLanguages = accountLanguageRepository.findAllByAccount(account);
        ProfileImg profileImg = accountProfileImgRepository.findOneByAccount(account).orElse(null);
        AccountNation accountNation = accountNationRepository.findOneByAccount(account).orElse(null);

        return EntityToDtoConverter.accountToInfoDto(account, accountInterests, accountLanguages, profileImg, accountNation);
    }

    /**
     * 로그인 사용자 정보 조회
     */
    @Transactional(readOnly = true)
    public AccountInfoResponse getUserWithAuthorities() {
        return accountToUserInfo(SecurityUtil.getCurrentUserName().flatMap(accountRepository::findOneWithAccountAuthoritiesByEmail).orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다.")));
    }

    /**
     * id로 AccountInfoResponse 반환
     */
    @Transactional(readOnly = true)
    public AccountInfoResponse  getUserWithAuthoritiesById(Long id) {
        return accountToUserInfo(accountRepository.findOneWithAccountAuthoritiesById(id).orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다.")));
    }

    /**
     * email로 UserInfoResponse 반환
     */
    @Transactional(readOnly = true)
    public AccountInfoResponse getUserWithAuthoritiesByEmail(String email) {
        return accountToUserInfo(accountRepository.findOneWithAccountAuthoritiesByEmail(email).orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다.")));
    }

    /**
     * 회원 이미지 업로드
     */
    public ProfileImgDto accountProfileImgUpload(MultipartFile multipartFile) throws IOException {
        // 회원 찾기
        Account account = SecurityUtil.getCurrentUserName().flatMap(accountRepository::findOneWithAccountAuthoritiesByEmail).orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        // 기존 이미지 제거
        Optional<ProfileImg> accountProfile = accountProfileImgRepository.findOneByAccount(account);
        if (accountProfile.isPresent()) {
            awsS3Uploader.delete(accountProfile.get().getUploadFileName());
            accountProfileImgRepository.delete(accountProfile.get());
        }

        // aws 파일 업로드
        FileInfo fileInfo = awsS3Uploader.upload(multipartFile, account.getEmail());
        ProfileImg profileImg = ProfileImg.of(account, fileInfo);

        // db에 이미지 정보 저장
        accountProfileImgRepository.save(profileImg);

        // profileImg를 profileImgDto로 변환 후 반환
        return EntityToDtoConverter.profileImgToProfileImgDto(profileImg);
    }

    /**
     * 회원 삭제
     */
    public void deleteAccount() {
        // 회원 찾기
        Account account = SecurityUtil.getCurrentUserName().flatMap(accountRepository::findOneWithAccountAuthoritiesByEmail).orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        // 회원 entity와 회원 관련 entity 삭제
        deleteAccount(account);
    }

    /**
     * email로 회원을 찾아서 삭제
     * @param email
     */
    public void deleteAccountByEmail(String email) {
        // 회원 찾기
        Account account = accountRepository.findOneByEmail(email).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        // 회원 entity와 회원 관련 entity 삭제
        deleteAccount(account);
    }

    /**
     * 회원 삭제를 위한 private 메소드
     * @param account
     */
    private void deleteAccount(Account account) {
        // 관련 entity 삭제
        accountNationRepository.deleteByAccount(account);
        accountAuthorityRepository.deleteByAccount(account);
        accountInterestRepository.deleteByAccount(account);
        accountLanguageRepository.deleteByAccount(account);
        accountProfileImgRepository.deleteByAccount(account);

        // 회원 삭제
        accountRepository.delete(account);
    }

    /**
     * 게시물 주인 정보 조회
     */
    public PostOwnerInfo getPostOwnerInfo(Long postId) {
        PostOwnerInfo postOwnerInfo = new PostOwnerInfo();

        // postId로 accountPost 찾아서 글 작성자를 찾음
        AccountPost accountPost = accountPostRepository.findOneByPostId(postId);
        Account postOwner = accountPost.getUser();

        // 글 작성자 정보 찾기
        Optional<AccountNation> nationOptional = accountNationRepository.findOneByAccount(postOwner);
        List<AccountLanguage> accountLanguages = accountLanguageRepository.findAllByAccount(postOwner);
        Optional<ProfileImg> profileImgOptional = accountProfileImgRepository.findOneByAccount(postOwner);

        // language 설정
        List<LanguageDto> languageDtoList = new ArrayList<>();
        for(AccountLanguage al : accountLanguages) {
            LanguageDto languageDto = new LanguageDto();
            languageDto.setName(al.getLanguage().getName());
            languageDto.setLevel(al.getLanguageLevel().getName());
            languageDtoList.add(languageDto);
        }

        // nation 설정
        if(nationOptional.isPresent()) {
            AccountNation accountNation = nationOptional.get();
            NationDto nationDto = new NationDto();
            nationDto.setCity(accountNation.getCity());
            nationDto.setName(accountNation.getNation().getName());
            postOwnerInfo.setNationDto(nationDto);
        }

        // profileDto 설정
        if(profileImgOptional.isPresent()) {
            ProfileImg profileImg = profileImgOptional.get();
            ProfileImgDto profileImgDto = new ProfileImgDto();
            profileImgDto.setEmail(profileImg.getAccount().getEmail());
            profileImgDto.setUploadFileName(profileImg.getUploadFileName());
            profileImgDto.setOriginalFileName(profileImg.getOriginalFileName());
            profileImgDto.setUploadFilePath(profileImg.getUploadFilePath());
            profileImgDto.setUploadFileUrl(profileImg.getUploadFileUrl());
            postOwnerInfo.setProfileImgDto(profileImgDto);
        }

        postOwnerInfo.setFirstName(postOwner.getFirstName());
        postOwnerInfo.setLastName(postOwner.getLastName());
//        postOwnerInfo.setGender(postOwner.getGender());
        postOwnerInfo.setLanguageDtoList(languageDtoList);

        return postOwnerInfo;
    }

    public PostOwnerInfo accountToPostOwnerInfo(Account account) {
        List<AccountLanguage> accountLanguages = accountLanguageRepository.findAllByAccount(account);
        ProfileImg profileImg = accountProfileImgRepository.findOneByAccount(account).orElse(null);
        AccountNation nation = accountNationRepository.findOneByAccount(account).orElse(null);
        return PostOwnerInfo.builder()
                .gender(account.getGender())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .nationDto(EntityToDtoConverter.nationToNationDto(nation))
                .profileImgDto(EntityToDtoConverter.profileImgToProfileImgDto(profileImg))
                .languageDtoList(accountLanguages.stream()
                        .map(EntityToDtoConverter::languageToLanguageDto)
                        .collect(Collectors.toList()))
                .build();

    }

    public Account getCurrentUser() {
        Account account = SecurityUtil.getCurrentUserName().flatMap(accountRepository::findOneWithAccountAuthoritiesByEmail).orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        return account;
    }

    /**
     * 현재 로그인된 유저 정보 반환 (PostOwnerInfo와 사용하는 필드 동일하여 재사용)
     */
    public PostOwnerInfo getCurrentUserInfo() {
        Account account = getCurrentUser();
        return accountToPostOwnerInfo(account);
    }
}
