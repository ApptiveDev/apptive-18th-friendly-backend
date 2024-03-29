package apptive.team1.friendly.global.common.jwt.config;

import apptive.team1.friendly.global.common.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${jwt.secret}")
    private String accessTokenSecret;

    @Value("${jwt.access_token_validity_in_seconds}")
    private Long accessTokenValidityInSeconds;

    /**
     * Access Token Provider
     * refresh token을 구현하기전까지는 accessToken의 유효기간을 365일로 함
     */
    @Bean(name = "tokenProvider")
    public JwtTokenProvider tokenProvider() {
        return new JwtTokenProvider(accessTokenSecret, accessTokenValidityInSeconds * 6 * 24 * 365);
    }

}
