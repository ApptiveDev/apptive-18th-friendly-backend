package apptive.team1.friendly.global.auth.local.controller;

import apptive.team1.friendly.global.auth.local.dto.LoginRequest;
import apptive.team1.friendly.global.auth.local.dto.LoginResponse;
import apptive.team1.friendly.global.auth.local.service.AuthService;
import apptive.team1.friendly.global.common.jwt.JwtFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 로그인 api
     */
    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> authorize(@Valid @RequestBody LoginRequest loginRequest) {

        LoginResponse accessToken = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + accessToken);

        return new ResponseEntity<>(accessToken, httpHeaders, HttpStatus.OK);
    }
}
