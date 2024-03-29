package apptive.team1.friendly.domain.user.data.entity;

import apptive.team1.friendly.domain.user.data.constant.LanguageLevel;
import apptive.team1.friendly.domain.user.data.vo.Language;
import apptive.team1.friendly.global.common.s3.AwsS3Uploader;
import apptive.team1.friendly.global.common.s3.FileInfo;
import apptive.team1.friendly.global.error.exception.Exception400;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
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
                   List<Language> languageObjects, List<String> languages, List<LanguageLevel> languageLevels, Authority authority, boolean activated,
                   String affiliation) {
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
        this.languageObjects = languageObjects;
        this.languages = languages;
        this.languageLevels = languageLevels;
        this.activated = activated;
        AccountAuthority accountAuthority = AccountAuthority.builder()
                .account(this)
                .authority(authority)
                .build();
        this.accountAuthorities.add(accountAuthority);
        this.affiliation = affiliation;
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
    private List<Language> languageObjects = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> languages = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private List<LanguageLevel> languageLevels = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> interests = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<AccountAuthority> accountAuthorities = new HashSet<>(); // 권한

    private boolean activated;  // 활성화 여부

    private String affiliation; // 소속

    public Account() {
        email = "";
        accountAuthorities = new HashSet<>();
    }

    //==========정적 생성 메서드==========//
    public static Account create(String email, String password, String firstName,
                              String lastName, String birthday, String gender,
                              String introduction, List<String> interests, String nation,
                              String city, List<String> languages, List<String> languageLevels, Authority authority,
                                 String affiliation) {

        if (languages.size() != languageLevels.size()) {
            throw new Exception400("언어 개수와 언어레벨 개수가 다릅니다.");
        }

        List<Language> languageList = new ArrayList<>();
        for (int i = 0; i < languages.size(); i++) {
            languageList.add(Language.builder()
                    .languageName(languages.get(i))
                    .languageLevel(LanguageLevel.getLevelByName(languageLevels.get(i)))
                    .build());
        }

        List<LanguageLevel> languageLevelList = new ArrayList<>();
        for (String languageLevelName : languageLevels) {
            LanguageLevel languageLevel = LanguageLevel.getLevelByName(languageLevelName);
            languageLevelList.add(languageLevel);
        }

        return Account.builder()
                .email(email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .birthday(birthday)
                .gender(gender)
                .introduction(introduction)
                .interests(interests)
                .nation(nation)
                .city(city)
                .languageObjects(languageList)
                .languages(languages)
                .languageLevels(languageLevelList)
                .authority(authority)
                .activated(true)
                .affiliation(affiliation)
                .build();
    }

    //===========비즈니스 로직===========//
    public void modify(String email, String password, String firstName,
                       String lastName, String birthday, String gender,
                       String introduction, List<String> interests, String nation,
                       String city, List<String> languages, List<String> languageLevels, boolean activated,
                       String affiliation) {
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
        this.affiliation = affiliation;

        if (languages.size() != languageLevels.size()) {
            throw new Exception400("언어 개수와 언어레벨 개수가 다릅니다.");
        }

        List<Language> languageList = new ArrayList<>();
        for (int i = 0; i < languages.size(); i++) {
            languageList.add(Language.builder()
                    .languageName(languages.get(i))
                    .languageLevel(LanguageLevel.getLevelByName(languageLevels.get(i)))
                    .build());
        }

        List<LanguageLevel> languageLevelList = new ArrayList<>();
        for (String languageLevelName : languageLevels) {
            LanguageLevel languageLevel = LanguageLevel.getLevelByName(languageLevelName);
            languageLevelList.add(languageLevel);
        }

        this.languageLevels = languageLevelList;
    }

    public void deleteProfileImage(AwsS3Uploader awsS3Uploader) {
        if(this.profileImg != null) {
            awsS3Uploader.delete(this.profileImg.getUploadFileName());
        }
        this.profileImg = null;
    }

    public ProfileImg uploadProfileImage(MultipartFile multipartFile, AwsS3Uploader awsS3Uploader) throws IOException {
        FileInfo fileInfo = awsS3Uploader.upload(multipartFile, this.getEmail());
        this.profileImg = ProfileImg.of(fileInfo);
        return this.profileImg;
    }
}
