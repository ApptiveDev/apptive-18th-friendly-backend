package apptive.team1.friendly.domain.user.data.dto;

import apptive.team1.friendly.domain.user.data.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {

    private String email;

    public static SignupResponse of(Account account) {
        if (account == null) return null;

        return SignupResponse.builder()
                .email(account.getEmail())
                .build();
    }
}
