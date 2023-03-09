package apptive.team1.friendly.domain.user.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;    // 로그인 id

    @Column(nullable = false)
    private String password;    // 비밀번호

    @Column(nullable = false)
    private String email;  // 이메일

    private String firstName;   // 이름

    private String lastName;    // 성

    private Long nation;    // 국가

    private Long language;  // 언어

    private Long interest;  // 하고 싶은 활동

    private Long favorite;  // 좋아하는 것

    private String introduction;    // 자기소개

    private Long gender; // 성별

    @OneToMany(mappedBy = "account")
    @Builder.Default
    private Set<AccountAuthority> accountAuthorities = new HashSet<>(); // 권한

    private boolean activated;  // 활성화 여부
}
