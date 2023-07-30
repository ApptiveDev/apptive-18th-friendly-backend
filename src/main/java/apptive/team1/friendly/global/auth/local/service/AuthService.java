package apptive.team1.friendly.global.auth.local.service;

import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.global.auth.local.dto.LoginResponse;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
import apptive.team1.friendly.global.common.jwt.JwtTokenProvider;
import apptive.team1.friendly.global.error.ErrorCode;
import apptive.team1.friendly.global.error.exception.CustomException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountRepository accountRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, AccountRepository accountRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.accountRepository = accountRepository;
    }

    /**
     * 자체 로그인
     * @param email
     * @param password
     * @return
     */
    public LoginResponse authenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        // 이 과정에서 CustomUserDetailsService 에서 우리가 재정의한 loadUserByUsername 메서드 호출
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.createToken(authentication);

        Account account = accountRepository.findOneByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return LoginResponse.builder()
                .accessToken(jwt)
                .userId(account.getId())
                .build();
    }
}
