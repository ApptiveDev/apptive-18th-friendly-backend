package apptive.team1.friendly.domain.user.data.entity;

import apptive.team1.friendly.domain.user.data.dto.SignupRequest;
import apptive.team1.friendly.domain.user.data.vo.Language;
import apptive.team1.friendly.global.common.s3.AwsS3Uploader;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity @Getter
@AllArgsConstructor
public class Account {
    @Builder
    public Account(String email, String password, String firstName,
                   String lastName, String birthday, String gender,
                   String introduction, List<String> interests, String nation, String city,
                   List<Language> languages, Authority authority, boolean activated) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.gender = gender;
        this.introduction = introduction;
        this.interests = interests;
        this.nation = nation;
        this.city = city;
        this.languages = languages;
        this.activated = activated;
        AccountAuthority accountAuthority = AccountAuthority.builder()
                .account(this)
                .authority(authority)
                .build();
        this.accountAuthorities.add(accountAuthority);
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_img_id")
    private ProfileImg profileImg;

    private String nation;

    private String city;

    @ElementCollection
    private List<Language> languages = new ArrayList<>();

    @ElementCollection
    private List<String> interests = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<AccountAuthority> accountAuthorities = new HashSet<>(); // 권한

    private boolean activated;  // 활성화 여부

    public Account() {
        email = "";
        accountAuthorities = new HashSet<>();
    }

    //==========정적 생성 메서드==========//
    public static Account create(SignupRequest signupRequest, Authority authority, PasswordEncoder passwordEncoder) {

        return Account.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .birthday(signupRequest.getBirthday())
                .gender(signupRequest.getGender())
                .introduction(signupRequest.getIntroduction())
                .interests(signupRequest.getInterests())
                .nation(signupRequest.getNation())
                .city(signupRequest.getCity())
                .languages(signupRequest.getLanguages())
                .authority(authority)
                .activated(true)
                .build();
    }

    //===========비즈니스 로직===========//
    public void extraSignup(String birthday, String firstName, String lastName,
                            String introduction, String gender, List<String> interests,
                            List<Language> languages, String nation, String city, boolean activated) {
        this.birthday = birthday;
        this.firstName = firstName;
        this.lastName = lastName;
        this.introduction = introduction;
        this.gender = gender;
        this.activated = activated;
        this.interests = interests;
        this.languages = languages;
        this.nation = nation;
        this.city = city;
    }

    public void deleteProfileImage(AwsS3Uploader awsS3Uploader) {
        if(this.profileImg != null) {
            awsS3Uploader.delete(this.profileImg.getUploadFileName());
        }
        this.profileImg = null;
    }

}
