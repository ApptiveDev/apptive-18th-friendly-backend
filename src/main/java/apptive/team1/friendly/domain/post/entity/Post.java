package apptive.team1.friendly.domain.post.entity;

import apptive.team1.friendly.domain.post.dto.UpdatePostDto;
import apptive.team1.friendly.domain.user.data.entity.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.invoke.MethodHandles.throwException;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    public Post(AccountPost accountPost, String title, String description, int maxPeople, LocalDateTime promiseTime, String location, List<String> rules, List<HashTag> hashTag) {
        accountPosts.add(accountPost);
        this.author = accountPost;
        this.title = title;
        this.description = description;
        this.maxPeople = maxPeople;
        this.promiseTime = promiseTime;
        this.location = location;
        this.rules = rules;
        this.hashTag = hashTag;
    }

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시판 리스트에 보이는 필드
    private String title;

    @Column(length = 500)
    private String description;

    private int maxPeople;

    private LocalDateTime promiseTime;

    private String location;

    private String image;

    // 게시글에 들어가야 보이는 필드
    @OneToOne
    private AccountPost author;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<HashTag> hashTag = new ArrayList<HashTag>();

    @ElementCollection
    private List<String> rules = new ArrayList<String>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private final List<AccountPost> accountPosts = new ArrayList<AccountPost>();


    /**
     * 인원 추가
     */
    public void addUser(AccountPost accountPost) {
        if(accountPosts.size() < maxPeople) {
            accountPosts.add(accountPost);
        }
        else {
//            throwException(Full);
        }
    }

    /**
     * 인원 삭제
     */
    public void quitUser(AccountPost accountPost) {
        if(accountPosts.contains(accountPost)) {
            accountPosts.remove(accountPost);
        }
        else {
//            throwException(invalidQuit);
        }
    }

    /**
     * 현재 방의 인원수
     */
    public int getPeopleCount() {
        return accountPosts.size();
    }

    /**
     * 게시물 업데이트
     * @param dto
     */
    public void change(UpdatePostDto dto) {
        
    }
}
