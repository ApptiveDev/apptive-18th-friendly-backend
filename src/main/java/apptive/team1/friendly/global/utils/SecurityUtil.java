package apptive.team1.friendly.global.utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Optional;

public class SecurityUtil {

    public SecurityUtil() {
    }

    /**
     * securityContext의 authentication 객체를 이용해 username을 반환해주는 유틸성 method
     */
    public static Optional<String> getCurrentUserName() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            System.out.println("Security Context에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        String username = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            username = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            username = (String) authentication.getPrincipal();
        }

        return Optional.ofNullable(username);
    }

    public static Authentication getAuthenticationByEmail(String email) {
        // Create a UserDetails object representing the authenticated user
        UserDetails userDetails = User.builder()
                .username(email)
                .password("unused")
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))) // Set appropriate authorities
                .build();

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
