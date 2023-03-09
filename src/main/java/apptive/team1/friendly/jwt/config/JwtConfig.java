package apptive.team1.friendly.jwt.config;

import apptive.team1.friendly.jwt.JwtTokenProvider;
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
     */
    @Bean(name = "tokenProvider")
    public JwtTokenProvider tokenProvider() {
        return new JwtTokenProvider(accessTokenSecret, accessTokenValidityInSeconds);
    }

}
