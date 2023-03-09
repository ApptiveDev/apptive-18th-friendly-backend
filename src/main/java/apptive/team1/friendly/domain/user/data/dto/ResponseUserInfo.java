package apptive.team1.friendly.domain.user.data.dto;

import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.entity.AccountAuthority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUserInfo {
    @Column(nullable = false, unique = true)
    private String username;    // 로그인 id

    private String email;  // 이메일

    private String firstName;   // 이름

    private String lastName;    // 성

    private Long nation;    // 국가

    private Long language;  // 언어

    private Long interest;  // 하고 싶은 활동

    private Long favorite;  // 좋아하는 것

    private String introduction;    // 자기소개

    private Long gender; // 성별

    private Set<String> accountAuthorities = new HashSet<>(); // 권한

    public static ResponseUserInfo of(Account account) {
        if (account == null) return null;

        return ResponseUserInfo.builder()
                .username(account.getUsername())
                .email(account.getEmail())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .nation(account.getNation())
                .language(account.getLanguage())
                .interest(account.getInterest())
                .favorite(account.getFavorite())
                .introduction(account.getIntroduction())
                .gender(account.getGender())
                .gender(account.getGender())
                .accountAuthorities(account.getAccountAuthorities().stream()
                        .map(accountAuthority -> accountAuthority.getAuthority().getAuthorityName())
                        .collect(Collectors.toSet()))
                .build();
    }
}
