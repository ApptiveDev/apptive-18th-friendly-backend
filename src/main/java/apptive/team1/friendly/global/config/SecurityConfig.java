package apptive.team1.friendly.global.config;

import apptive.team1.friendly.global.common.jwt.JwtAccessDeniedHandler;
import apptive.team1.friendly.global.common.jwt.JwtAuthenticationEntryPoint;
import apptive.team1.friendly.global.common.jwt.JwtTokenProvider;
import apptive.team1.friendly.global.common.jwt.config.JwtSecurityConfig;
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
                .antMatchers("/api/hello").permitAll()  // test api hello 인증이 없어도 접근 가능
                .antMatchers("/api/user/auth", "/api/user/signup").permitAll()  // 로그인 인증이 없어도 접근 가능
                .antMatchers("/api/google/login", "/api/google/signup").permitAll()
                .antMatchers("/auth/social/GOOGLE", "/auth/social/KAKAO").permitAll()
                .antMatchers("/auth/social/GOOGLE/callback", "/auth/social/KAKAO/callback").permitAll()
                .antMatchers("/posts").permitAll()
                .antMatchers("/posts/*").permitAll()
                .antMatchers("/posts/*/create").permitAll()
                .antMatchers("/posts/create").permitAll()
                .antMatchers("/posts/*/edit").permitAll()
                .antMatchers("/posts/*/comments").permitAll()
                .antMatchers("api/user/myinfo").permitAll()
                .antMatchers("api/user/delete").permitAll()
                .antMatchers("/api/user/profileImgUpload").permitAll()
                .antMatchers("/api/user/delete/*").permitAll()
                .anyRequest().authenticated()
                // jwt filter config
                .and()
                .apply(new JwtSecurityConfig(jwtTokenProvider));
    }
}
