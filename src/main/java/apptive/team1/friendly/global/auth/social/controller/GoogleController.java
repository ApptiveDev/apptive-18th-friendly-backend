package apptive.team1.friendly.global.auth.social.controller;

import apptive.team1.friendly.domain.user.service.UserService;
import apptive.team1.friendly.global.auth.social.dto.GoogleAuthRequest;
import apptive.team1.friendly.global.auth.social.dto.SocialAuthResponse;
import apptive.team1.friendly.global.auth.social.service.SocialAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
@RequestMapping(value = "/api/google")
@RequiredArgsConstructor
public class GoogleController {

    private final SocialAuthService socialAuthService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<SocialAuthResponse> googleLogin(@RequestBody GoogleAuthRequest googleAuthRequest) throws GeneralSecurityException, IOException {
        String idToken = googleAuthRequest.getId_token();

        SocialAuthResponse loginResponse = socialAuthService.googleLogin(idToken);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}