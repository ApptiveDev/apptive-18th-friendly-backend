package apptive.team1.friendly.domain.user.service;

import apptive.team1.friendly.domain.curation.repository.ContentRepository;
import apptive.team1.friendly.domain.post.repository.PostRepository;
import apptive.team1.friendly.domain.user.data.dto.*;
import apptive.team1.friendly.domain.user.data.entity.ProfileImg;
import apptive.team1.friendly.global.common.s3.AwsS3Uploader;
import apptive.team1.friendly.global.common.s3.FileInfo;
import apptive.team1.friendly.domain.user.data.dto.AccountInfoResponse;
import apptive.team1.friendly.domain.user.data.dto.GoogleSignUpRequest;
import apptive.team1.friendly.domain.user.data.dto.SignupRequest;
import apptive.team1.friendly.domain.user.data.dto.SignupResponse;
import apptive.team1.friendly.domain.user.data.dto.profile.EntityToDtoConverter;
import apptive.team1.friendly.domain.user.data.dto.profile.ProfileImgDto;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.entity.Authority;
import apptive.team1.friendly.domain.user.data.repository.*;
import apptive.team1.friendly.global.error.ErrorCode;
import apptive.team1.friendly.global.error.exception.CustomException;
import apptive.team1.friendly.global.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final AccountRepository accountRepository;
    private final AuthorityRepository authorityRepository;
    private final PostRepository postRepository;
    private final ContentRepository contentRepository;
    private final PasswordEncoder passwordEncoder;
    private final AwsS3Uploader awsS3Uploader;

    /**
     * 회원가입
     */
    @Transactional
    public SignupResponse signUp(SignupRequest signupRequest) {

        Authority authority = authorityRepository.getReferenceById("ROLE_USER");

        Account account = Account.create(signupRequest.getEmail(), passwordEncoder.encode(signupRequest.getPassword()),
                signupRequest.getFirstName(), signupRequest.getLastName(),
                signupRequest.getBirthday(), signupRequest.getGender(), signupRequest.getIntroduction(),
                signupRequest.getInterests(), signupRequest.getNation(), signupRequest.getCity(),
                signupRequest.getLanguages(), authority);

        return SignupResponse.of(accountRepository.save(account));
    }

    /**
     * 추가정보 회원가입
     * 이미 로그인에서 임시로 회원가입 해둔 회원을 찾아야함
     * 비밀번호 설정이 따로 필요없음
     */
    @Transactional
    public SignupResponse extraSignUp(GoogleSignUpRequest signupRequest) {

        Account account = getCurrentUser();

        account.extraSignup(signupRequest.getBirthday(), signupRequest.getFirstName(),
                signupRequest.getLastName(), signupRequest.getIntroduction(),
                signupRequest.getGender(), signupRequest.getInterests(), signupRequest.getLanguages(),
                signupRequest.getNation(), signupRequest.getCity(), true);

        return SignupResponse.of(accountRepository.save(account));
    }

    /**
     * Account 객체를 UserInfoResponse로 반환
     */
    public AccountInfoResponse accountToUserInfo(Account account) {
        return EntityToDtoConverter.accountToUserInfoDto(account);
    }

    /**
     * 로그인 사용자 정보 조회
     */
    @Transactional(readOnly = true)
    public AccountInfoResponse getUserWithAuthorities() {
        return accountToUserInfo(getCurrentUser());
    }

    /**
     * id로 AccountInfoResponse 반환
     */
    @Transactional(readOnly = true)
    public AccountInfoResponse  getUserWithAuthoritiesById(Long id) {
        return accountToUserInfo(accountRepository.findOneWithAccountAuthoritiesById(id).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)));
    }

    /**
     * email로 UserInfoResponse 반환
     */
    @Transactional(readOnly = true)
    public AccountInfoResponse getUserWithAuthoritiesByEmail(String email) {
        return accountToUserInfo(accountRepository.findOneWithAccountAuthoritiesByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)));
    }

    /**
     * 회원 이미지 업로드
     */
    public ProfileImgDto accountProfileImgUpload(MultipartFile multipartFile) throws IOException {
        // 회원 찾기
        Account account = getCurrentUser();

        // 기존 이미지 제거
        account.deleteProfileImage(awsS3Uploader);

        // aws 파일 업로드
        ProfileImg profileImg = account.uploadProfileImage(multipartFile, awsS3Uploader);

        // db에 저장
        accountRepository.save(account);

        // profileImg를 profileImgDto로 변환 후 반환
        return EntityToDtoConverter.profileImgToProfileImgDto(account, profileImg);
    }

    /**
     * 회원 삭제
     */
    public void deleteAccount() {
        // 회원 찾기
        Account account = getCurrentUser();

        // 회원 entity와 회원 관련 entity 삭제
        postRepository.deleteAllByUser(account);
        contentRepository.deleteAllByUser(account);
        accountRepository.delete(account);
    }

    /**
     * email로 회원을 찾아서 삭제
     * @param email
     */
    public void deleteAccountByEmail(String email) {
        // 회원 찾기
        Account account = accountRepository.findOneByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 회원 entity와 회원 관련 entity 삭제
        postRepository.deleteAllByUser(account);
        accountRepository.delete(account);
    }

    /**
     * 게시물 주인 정보 조회
     */
    public UserInfo getPostOwnerInfo(Long postId) {

        Account postOwner = accountRepository.findAuthorByPostId(postId);

        if(postOwner == null) // 삭제한 유저
            return null;

        return UserInfo.create(postOwner.getEmail(), postOwner.getFirstName(), postOwner.getLastName(),
                postOwner.getGender(), postOwner.getNation(), postOwner.getCity(), postOwner.getLanguages(), postOwner.getProfileImg());
    }

    /**
     * 현재 로그인된 유저 정보 반환
     */
    public UserInfo getCurrentUserInfo() {

        Account account = getCurrentUser();

        return accountToOwnerInfo(account);
    }

    public UserInfo accountToOwnerInfo(Account account) {

        if(account == null)
            return null;

        return UserInfo.builder()
                .gender(account.getGender())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .nation(account.getNation())
                .profileImg(account.getProfileImg())
                .languages(account.getLanguages())
                .build();
    }

    public Account getCurrentUser() {

        Account account = SecurityUtil.getCurrentUserName().flatMap(accountRepository::findOneWithAccountAuthoritiesByEmail).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Hibernate.initialize(account.getLanguages());
        Hibernate.initialize(account.getInterests());

        return account;
    }

}
