package apptive.team1.friendly.domain.user.controller;

import apptive.team1.friendly.domain.user.data.dto.RequestSignUp;
import apptive.team1.friendly.domain.user.data.dto.ResponseSignUp;
import apptive.team1.friendly.domain.user.data.dto.ResponseUserInfo;
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
    public ResponseEntity<ResponseSignUp> signup(@Valid @RequestBody RequestSignUp requestSignUp) {
        ResponseSignUp responseSignUp = userService.signUp(requestSignUp);

        return new ResponseEntity<>(responseSignUp, HttpStatus.OK);
    }

    /**
     * 본인 정보 조회 api
     */
    @GetMapping("/myinfo")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseUserInfo> getMyUserInfo() {
        return new ResponseEntity<>(userService.getUserWithAuthorities(), HttpStatus.OK);
    }

    /**
     * ADMIN 전용 회원 조회 api
     */
    @GetMapping("/info/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseUserInfo> getUserInfo(@PathVariable String username) {
        return new ResponseEntity<>(userService.getUserWithAuthorities(username), HttpStatus.OK);
    }
}
