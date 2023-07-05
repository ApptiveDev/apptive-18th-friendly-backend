package apptive.team1.friendly.domain.post.dto;

import apptive.team1.friendly.domain.post.entity.HashTag;
import apptive.team1.friendly.domain.post.entity.PostImage;
import apptive.team1.friendly.domain.user.data.dto.PostOwnerInfo;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class PostFormDto {

    /**
     * 새로 글 쓰는 경우 생성자. postId가 없음
     */
    @Builder
    public PostFormDto(String title, Set<HashTag> hashTag, int maxPeople, String description, LocalDateTime promiseTime, String location, Set<String> rules) {
        this.title = title;
        this.hashTag = hashTag;
        this.maxPeople = maxPeople;
        this.description = description;
        this.promiseTime = promiseTime;
        this.location = location;
        this.rules = rules;
    }

    private String title;

    private Set<HashTag> hashTag = new HashSet<>();

    private int maxPeople;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime promiseTime;

    private String location;

    private Set<String> rules = new HashSet<>();
}
