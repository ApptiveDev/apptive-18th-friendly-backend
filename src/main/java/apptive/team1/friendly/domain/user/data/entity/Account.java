package apptive.team1.friendly.domain.user.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
public class Account {
    @Builder
    public Account(String email, String password, String firstName,
                   String lastName, String birthday, String gender,
                   String introduction, Set<AccountAuthority> accountAuthorities, boolean activated) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.gender = gender;
        this.introduction = introduction;
        this.accountAuthorities = accountAuthorities;
        this.activated = activated;
    }

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;  // 이메일 (username 겸함)

    private String password;    // 비밀번호

    private String firstName;   // 이름

    private String lastName;    // 성

    private String birthday;    // 생일

    private String gender;

    private String introduction;    // 자기소개

    @OneToMany(mappedBy = "account")
    @Builder.Default
    private Set<AccountAuthority> accountAuthorities = new HashSet<>(); // 권한

    private boolean activated;  // 활성화 여부

    public Account() {
        email = "";
        accountAuthorities = new HashSet<>();
    }
}
