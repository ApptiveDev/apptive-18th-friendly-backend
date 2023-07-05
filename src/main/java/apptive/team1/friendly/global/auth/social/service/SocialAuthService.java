package apptive.team1.friendly.global.auth.social.service;

import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.entity.AccountAuthority;
import apptive.team1.friendly.domain.user.data.entity.Authority;
import apptive.team1.friendly.domain.user.data.repository.AccountAuthorityRepository;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
import apptive.team1.friendly.domain.user.data.repository.AuthorityRepository;
import apptive.team1.friendly.global.auth.social.GoogleOauth;
import apptive.team1.friendly.global.auth.social.data.Constant;
import apptive.team1.friendly.global.auth.social.dto.GoogleAuthRequest;
import apptive.team1.friendly.global.auth.social.dto.GoogleUser;
import apptive.team1.friendly.global.auth.social.dto.SocialAuthResponse;
import apptive.team1.friendly.global.common.jwt.JwtTokenProvider;
import apptive.team1.friendly.global.utils.SecurityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional
public class SocialAuthService {
    private final HttpServletResponse response;

    private final GoogleOauth googleOauth;
    private final JwtTokenProvider jwtTokenProvider;

    private final AccountRepository accountRepository;
    private final AuthorityRepository authorityRepository;
    private final AccountAuthorityRepository accountAuthorityRepository;

    /**
     * 소셜 로그인을 위해 redirectUrl로 redirect
     * @param socialLoginType
     * @throws IOException
     */
    public void request(Constant.SocialLoginType socialLoginType) throws IOException {
        String redirectURL;
        switch (socialLoginType) {
            case GOOGLE:{
                redirectURL = googleOauth.getOauthRedirectUrl();
            } break;
            default:{
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }
        }

        response.sendRedirect(redirectURL);
    }

    /**
     * 소셜 로그인 by code
     * @param socialLoginType
     * @param code
     * @return
     * @throws JsonProcessingException
     */
    public SocialAuthResponse OAuthLogin(Constant.SocialLoginType socialLoginType, String code) throws JsonProcessingException {
        switch (socialLoginType) {
            case GOOGLE:{
                // 일회성 코드로 구글로부터 엑세스 토큰이 담긴 응답객체 받기
                ResponseEntity<String> accessTokenResponse = googleOauth.requestAccessToken(code);
                // 응답 객체가 JSON 형식으로 되어 있어서, 이를 역직렬화(Deserialization)해서 자바 객체에 담기
                GoogleAuthRequest googleAuthRequest = googleOauth.getAccessToken(accessTokenResponse);

                // 엑세스 토큰을 통해 구글에서 사용자 정보가 담긴 응답객체 받기
                ResponseEntity<String> userInfoResponse = googleOauth.requestUserInfo(googleAuthRequest);
                // 사용자 정보를 자바 객체에 담기
                GoogleUser googleUser = googleOauth.getUserInfo(userInfoResponse);

                String email = googleUser.getEmail();
                return loginByEmail(email);
            }
            default:{
                throw new IllegalArgumentException("알 수 없는 로그인 형식입니다.");
            }
        }
    }

    /**
     * 소셜 로그인 by idToken
     * @param idTokenString
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public SocialAuthResponse googleLogin(String idTokenString) throws GeneralSecurityException, IOException {

        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(googleOauth.getGoogleClientId()))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();

        // (Receive idTokenString by HTTPS POST)
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String userId = payload.getSubject();
                String email = payload.getEmail();
                boolean emailVerified = payload.getEmailVerified();
                String name = (String) payload.get("name");

                return loginByEmail(email);
            } else {
                throw new RuntimeException("Invalid ID token.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Invalid ID token.");
        }
    }

    /**
     * 회원을 찾아서 케이스에 따라 로그인 진행
     */
    public SocialAuthResponse loginByEmail(String email) {
        Account account = accountRepository.findOneWithAccountAuthoritiesByEmail(email).orElse(null);
        SocialAuthResponse response = socialLoginByEmail(email);

        // 처음 사용자일때
        if (account == null) {
            tempSignup(email);
            response.setRegistered(false);
            return response;
        }
        // 회원 가입을 완료하지 않은 사용자일때
        else if (!account.isActivated()){
            response.setRegistered(false);
            return response;
        }
        // 기존 사용자일때
        else {
            response.setRegistered(true);
            return response;
        }
    }

    /**
     * 이메일로 authentication 객체 생성 후 jwt 토큰이 담긴 response 객체 반환
     * @param email
     * @return
     */
    public SocialAuthResponse socialLoginByEmail(String email) {
        Authentication authentication = SecurityUtil.getAuthenticationByEmail(email);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.createToken(authentication);

        return SocialAuthResponse.builder()
                .accessToken(jwt)
                .build();
    }

    /**
     * 소셜 임시 회원가입
     */
    public void tempSignup(String email) {
        Authority authority = authorityRepository.getReferenceById("ROLE_USER");

        Account user = Account.builder()
                .email(email)
                .activated(false)
                .build();

        AccountAuthority accountAuthority = AccountAuthority.builder()
                .account(user)
                .authority(authority)
                .build();

        authority.getAccountAuthorities().add(accountAuthority);
        user.getAccountAuthorities().add(accountAuthority);

        authorityRepository.save(authority);
        accountRepository.save(user);
        accountAuthorityRepository.save(accountAuthority);
    }
}
