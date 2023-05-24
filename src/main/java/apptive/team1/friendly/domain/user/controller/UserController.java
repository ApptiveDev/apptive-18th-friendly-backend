package apptive.team1.friendly.domain.user.controller;

import apptive.team1.friendly.domain.user.data.dto.AccountInfoResponse;
import apptive.team1.friendly.domain.user.data.dto.GoogleSignUpRequest;
import apptive.team1.friendly.domain.user.data.dto.SignupRequest;
import apptive.team1.friendly.domain.user.data.dto.SignupResponse;
import apptive.team1.friendly.domain.user.data.dto.profile.ProfileImgDto;
import apptive.team1.friendly.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입 api
     */
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        SignupResponse signupResponse = userService.signUp(signupRequest);

        return new ResponseEntity<>(signupResponse, HttpStatus.OK);
    }

    /**
     * 소셜 로그인 추가 회원가입 api
     */
    @PostMapping("/extraSignup")
    public ResponseEntity<SignupResponse> extraSignup(@RequestBody GoogleSignUpRequest googleSignUpRequest, HttpServletRequest httpServletRequest) {
        System.out.println("Header: " + httpServletRequest.getHeader("Authorization"));
        System.out.println(googleSignUpRequest.toString());
        SignupResponse signupResponse = userService.extraSignUp(googleSignUpRequest);

        return new ResponseEntity<>(signupResponse, HttpStatus.OK);
    }

    /**
     * 본인 정보 조회 api
     */
    @GetMapping("/myinfo")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AccountInfoResponse> getMyAccountInfo() {
        return new ResponseEntity<>(userService.getUserWithAuthorities(), HttpStatus.OK);
    }

    /**
     * id 기반 회원 조회 api
     */
    @GetMapping("/info/id/{id}")
    public ResponseEntity<AccountInfoResponse> getAccountInfoById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserWithAuthoritiesById(id), HttpStatus.OK);
    }

    /**
     * ADMIN 전용 회원 조회 api
     */
    @GetMapping("/info/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<AccountInfoResponse> getUserInfo(@PathVariable String email) {
        return new ResponseEntity<>(userService.getUserWithAuthoritiesByEmail(email), HttpStatus.OK);
    }

    /**
     * 회원 이미지 업로드 api
     */
    @PostMapping("/profileImgUpload")
    public ResponseEntity<ProfileImgDto> profileImgUpload(@RequestParam("img") MultipartFile multipartFile) throws IOException {
        ProfileImgDto profileImgDto = userService.accountProfileImgUpload(multipartFile);
        return new ResponseEntity<>(profileImgDto, HttpStatus.OK);
    }

    /**
     * 회원 삭제 (본인)
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAccount() {
        userService.deleteAccount();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 이메일로 회원 삭제
     */
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<Void> deleteAccountByEmail(@PathVariable String email) {
        userService.deleteAccountByEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
