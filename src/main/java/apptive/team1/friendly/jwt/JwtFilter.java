package apptive.team1.friendly.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtTokenProvider jwtTokenProvider;

    public JwtFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 실제 필터링 로직은 doFilter 안에 들어가게 된다. GenericFilterBean을 extends 해서 override
     * 토큰의 인증정보를 SecurityContext에 저장
     * DB를 거치지 않으므로 DB에 사용자가 존재하는지 여부를  알 수 없다.
     * @param servletRequest  The request to process
     * @param servletResponse The response associated with the request
     * @param filterChain    Provides access to the next filter in the chain for this
     *                 filter to pass the request and response to for further
     *                 processing
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        // token 유효성 검증
        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            // 토큰에서 username, authority 를 얻어서 spring security user를 생성하고 Authentication을 return
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
            // 해당 user를 security context에 저장 (DB를 거치지 않는다)
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Security Context에 '" + authentication.getName() + "' 인증 정보를 저장했습니다, uri: " + requestURI);
        } else {
            System.out.println("유효한 JWT 토큰이 없습니다, uri: " + requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * request에서 token 정보를 얻는다.
     * @param request
     * @return jwtToken
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
