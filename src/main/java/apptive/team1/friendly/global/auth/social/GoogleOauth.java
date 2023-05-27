package apptive.team1.friendly.global.auth.social;

import apptive.team1.friendly.global.auth.social.dto.GoogleAuthRequest;
import apptive.team1.friendly.global.auth.social.dto.GoogleUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Getter
@RequiredArgsConstructor
public class GoogleOauth implements SocialOauth{
    @Value("${google.auth.url}")
    private String googleAuthUrl;

    @Value("${google.token.request.url}")
    private String googleTokenRequestUrl;

    @Value("${google.redirect.uri}")
    private String googleRedirectUrl;

    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.client.secret}")
    private String googleSecret;

    @Value("${google.auth.scope}")
    private String scopes;

    private final ObjectMapper objectMapper;

    // Google 로그인 URL 생성 로직
    @Override
    public String getOauthRedirectUrl() {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", getGoogleClientId());
        params.put("redirect_uri", getGoogleRedirectUrl());
        params.put("response_type", "code");
        params.put("scope", getScopeUrl());

        // parameter에 따라 string 구성
        String paramStr = params.entrySet().stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));

        String redirectURL = getGoogleAuthUrl() + "?" + paramStr;

        return redirectURL;
    }

    // scope 의 값을 보내기 위해 띄어쓰기 값을 UTF-8로 변환하는 로직
    public String getScopeUrl() {
        return scopes.replaceAll(",", "%20");
    }

    /**
     * 구글로 코드를 보내 엑세스 토큰이 담긴 응답객체 받아서 반환
     */
    public ResponseEntity<String> requestAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", getGoogleClientId());
        params.put("client_secret", getGoogleSecret());
        params.put("redirect_uri", getGoogleRedirectUrl());
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(getGoogleTokenRequestUrl(), params, String.class);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return null;
        }

        return responseEntity;
    }

    public GoogleAuthRequest getAccessToken(ResponseEntity<String> accessTokenResponse) throws JsonProcessingException {
        return objectMapper.readValue(accessTokenResponse.getBody(), GoogleAuthRequest.class);
    }

    public ResponseEntity<String> requestUserInfo(GoogleAuthRequest googleAuthRequest) {
        final String GOOGLE_USERINFO_REQUEST_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
        RestTemplate restTemplate = new RestTemplate();

        // header에 accessToken 담기
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + googleAuthRequest.getAccess_token());

        // HttpEntity를 생성하고 header를 담기
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        // 구글로 request를 보내서 사용자 정보가 담긴 response 받기
        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);

        return response;
    }

    public GoogleUser getUserInfo(ResponseEntity<String> userInfoResponse) throws JsonProcessingException {
        GoogleUser googleUser = objectMapper.readValue(userInfoResponse.getBody(), GoogleUser.class);

        return googleUser;
    }
}