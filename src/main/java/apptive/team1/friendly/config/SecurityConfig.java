package apptive.team1.friendly.config;

import apptive.team1.friendly.jwt.JwtAccessDeniedHandler;
import apptive.team1.friendly.jwt.JwtAuthenticationEntryPoint;
import apptive.team1.friendly.jwt.JwtTokenProvider;
import apptive.team1.friendly.jwt.config.JwtSecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity  // web 보안 활성화
@EnableGlobalMethodSecurity(prePostEnabled = true)  // @PreAuthorize 어노테이션 사용을 위해 선언
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 토큰을 사용하기 때문에 csrf 설정 disable
                .csrf().disable()

                // exception handler 설정
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)  // custom authenticationEntryPoint
                .accessDeniedHandler(jwtAccessDeniedHandler)    // custom accessDeniedHandler
                // session을 사용하지 않기 때문에 STATELESS
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // api 경로 설정
                .and()
                .authorizeRequests()
                .antMatchers("/api/hello").permitAll()  // hello 인증이 없어도 접근 가능
                .antMatchers("/api/user/authenticate").permitAll()  // 로그인 인증이 없어도 접근 가능
                .antMatchers("/api/user/signup").permitAll() // 회원가입 인증이 없어도 접근 가능
                .anyRequest().authenticated()
                // jwt filter config
                .and()
                .apply(new JwtSecurityConfig(jwtTokenProvider));

    }
}
