package apptive.team1.friendly.domain.user.controller;

import apptive.team1.friendly.domain.user.data.dto.SignupRequest;
import apptive.team1.friendly.domain.user.data.dto.SignupResponse;
import apptive.team1.friendly.domain.user.data.dto.UserInfoResponse;
import apptive.team1.friendly.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
     * 본인 정보 조회 api
     */
    @GetMapping("/myinfo")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserInfoResponse> getMyUserInfo() {
        return new ResponseEntity<>(userService.getUserWithAuthorities(), HttpStatus.OK);
    }

    /**
     * ADMIN 전용 회원 조회 api
     */
    @GetMapping("/info/{email}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserInfoResponse> getUserInfo(@PathVariable String email) {
        return new ResponseEntity<>(userService.getUserWithAuthoritiesByEmail(email), HttpStatus.OK);
    }
}
