package apptive.team1.friendly.domain.user.data.dto;

import apptive.team1.friendly.domain.user.data.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSignUp {

    private String username;
    private Set<String> authoritySet;

    public static ResponseSignUp of(Account account) {
        if (account == null) return null;

        return ResponseSignUp.builder()
                .username(account.getUsername())
                .authoritySet(account.getAccountAuthorities().stream()
                        .map(accountAuthority -> accountAuthority.getAuthority().getAuthorityName())
                        .collect(Collectors.toSet()))
                .build();
    }
}
