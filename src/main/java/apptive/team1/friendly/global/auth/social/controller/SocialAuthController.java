package apptive.team1.friendly.global.auth.social.controller;

import apptive.team1.friendly.global.auth.social.data.Constant;
import apptive.team1.friendly.global.auth.social.dto.SocialAuthResponse;
import apptive.team1.friendly.global.auth.social.service.SocialAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/auth/social")
public class SocialAuthController {

    private final SocialAuthService socialAuthService;

    public SocialAuthController(SocialAuthService socialAuthService) {
        this.socialAuthService = socialAuthService;
    }

    @GetMapping("/{socialLoginType}")
    public void socialLoginRedirect(@PathVariable(name = "socialLoginType") String socialLoginPath) throws IOException {
        Constant.SocialLoginType socialLoginType = Constant.SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        socialAuthService.request(socialLoginType);
    }

    @ResponseBody
    @GetMapping(value = "/{socialLoginType}/callback")
    public ResponseEntity<SocialAuthResponse> callback(@PathVariable(name = "socialLoginType") String socialLoginPath,
                                                       @RequestParam(name = "code") String code) throws JsonProcessingException {
        Constant.SocialLoginType socialLoginType = Constant.SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        SocialAuthResponse authResponse = socialAuthService.OAuthLogin(socialLoginType, code);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
