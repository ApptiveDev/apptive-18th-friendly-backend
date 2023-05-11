package apptive.team1.friendly.domain.user.service;

import apptive.team1.friendly.config.GoogleOauth;
import apptive.team1.friendly.domain.user.data.dto.GoogleLoginResponse;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.entity.AccountAuthority;
import apptive.team1.friendly.domain.user.data.entity.Authority;
import apptive.team1.friendly.domain.user.data.repository.AccountAuthorityRepository;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
import apptive.team1.friendly.domain.user.data.repository.AuthorityRepository;
import apptive.team1.friendly.common.jwt.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleAuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleOauth googleOauth;
    private final AccountRepository accountRepository;
    private final AuthorityRepository authorityRepository;
    private final AccountAuthorityRepository accountAuthorityRepository;

    public GoogleAuthService(JwtTokenProvider jwtTokenProvider, GoogleOauth googleOauth, AccountRepository accountRepository, AuthorityRepository authorityRepository, AccountAuthorityRepository accountAuthorityRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.googleOauth = googleOauth;
        this.accountRepository = accountRepository;
        this.authorityRepository = authorityRepository;
        this.accountAuthorityRepository = accountAuthorityRepository;
    }

    public GoogleLoginResponse googleLogin(String idTokenString) throws GeneralSecurityException, IOException {

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
                Payload payload = idToken.getPayload();

                String userId = payload.getSubject();
                String email = payload.getEmail();
                boolean emailVerified = payload.getEmailVerified();
                String name = (String) payload.get("name");

                // Other user attributes as needed
//            String pictureUrl = (String) payload.get("picture");
//            String locale = (String) payload.get("locale");
//            String familyName = (String) payload.get("family_name");
//            String givenName = (String) payload.get("given_name");

                Account account = accountRepository.findOneWithAccountAuthoritiesByEmail(email).orElse(null);
                // 처음 사용자일때
                if (account == null) {
                    tempSignup(email);
                    GoogleLoginResponse response = googleLoginByGoogleIdToken(idTokenString, email);
                    response.setRegistered(false);
                    return response;
                }
                // 회원 가입을 완료하지 않은 사용자일때
                else if (!account.isActivated()){
                    GoogleLoginResponse response = googleLoginByGoogleIdToken(idTokenString, email);
                    response.setRegistered(false);
                    return response;
                }
                // 기존 사용자일때
                else {
                    GoogleLoginResponse response = googleLoginByGoogleIdToken(idTokenString, email);
                    response.setRegistered(true);
                    return response;
                }
            } else {
                throw new RuntimeException("Invalid ID token.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Invalid ID token.");
        }
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

        accountAuthorityRepository.save(accountAuthority);
        authorityRepository.save(authority);
        accountRepository.save(user);
    }

    /**
     *  구글 로그인 이후 서버용 토큰 생성
     */
    public GoogleLoginResponse googleLoginByGoogleIdToken(String idToken, String email) {
        Authentication authentication = getAuthenticationByGoogleIdToken(idToken, email);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.createToken(authentication);

        return GoogleLoginResponse.builder()
                .accessToken(jwt)
                .build();
    }


    /**
     * Google id Token -> Authentication
     */
    public Authentication getAuthenticationByGoogleIdToken(String idToken, String email) {
        // Verify the Google ID token and extract user's information
        // Use your existing logic to verify the ID token and extract user data

        // Create a UserDetails object representing the authenticated user
        UserDetails userDetails = User.builder()
                .username(email)
                .password("unused")
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))) // Set appropriate authorities
                .build();

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
