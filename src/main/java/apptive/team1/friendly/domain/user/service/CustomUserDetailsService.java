package apptive.team1.friendly.domain.user.service;

import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailService")
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public CustomUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * username을 통해서 userDetails.User 객체를 반환한다.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findOneWithAccountAuthoritiesByUsername(username)    // DB에서 user와 authorities를 가져온다.
                .map(account -> createUser(username, account))                    // 활성화상태라면 userdetails.User 객체를 반환한다.
                .orElseThrow(()-> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    /**
     * user가 활성화상태라면 권한정보와 password를 통해서 userDetails.User 객체를 return
     */
    private User createUser(String username, Account account) {
        if (!account.isActivated()) {
            throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
        }
        List<GrantedAuthority> grantedAuthorities = account.getAccountAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority().getAuthorityName()))
                .collect(Collectors.toList());

        return new User(account.getUsername(), account.getPassword(), grantedAuthorities);
    }
}
