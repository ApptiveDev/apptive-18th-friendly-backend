package apptive.team1.friendly.domain.user.controller;

import apptive.team1.friendly.config.GoogleOauth;
import apptive.team1.friendly.domain.user.data.dto.*;
import apptive.team1.friendly.domain.user.service.GoogleAuthService;
import apptive.team1.friendly.domain.user.service.UserService;
import apptive.team1.friendly.common.jwt.JwtTokenProvider;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
@RequestMapping(value = "/api/google")
public class GoogleController {

    private final GoogleOauth googleOauth;
    private final GoogleAuthService googleAuthService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    GoogleController(GoogleOauth googleOauth, GoogleAuthService googleAuthService, UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.googleOauth = googleOauth;
        this.googleAuthService = googleAuthService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<GoogleLoginResponse> googleLogin(@RequestBody GoogleLoginRequest googleLoginRequest) throws GeneralSecurityException, IOException {
        String idToken = googleLoginRequest.getIdToken();

        GoogleLoginResponse loginResponse = googleAuthService.googleLogin(idToken);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> googleSignup(@RequestBody GoogleSignUpRequest googleSignUpRequest, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");

        SignupResponse signupResponse = userService.googleSignUp(googleSignUpRequest, token);

        return new ResponseEntity<>(signupResponse, HttpStatus.OK);
    }
}