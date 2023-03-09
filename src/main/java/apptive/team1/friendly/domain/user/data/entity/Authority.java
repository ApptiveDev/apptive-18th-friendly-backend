package apptive.team1.friendly.domain.user.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority {
    @Id
    @Column(name = "authority_name")
    private String authorityName;

    @OneToMany(mappedBy = "authority")
    @Builder.Default
    private Set<AccountAuthority> accountAuthorities = new HashSet<>();
}
