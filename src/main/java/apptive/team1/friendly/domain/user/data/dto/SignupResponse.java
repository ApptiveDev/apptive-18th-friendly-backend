package apptive.team1.friendly.domain.user.data.dto;

import apptive.team1.friendly.domain.user.data.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {

    private String username;
    private Set<String> authoritySet;

    public static SignupResponse of(Account account) {
        if (account == null) return null;

        return SignupResponse.builder()
                .username(account.getUsername())
                .authoritySet(account.getAccountAuthorities().stream()
                        .map(accountAuthority -> accountAuthority.getAuthority().getAuthorityName())
                        .collect(Collectors.toSet()))
                .build();
    }
}
