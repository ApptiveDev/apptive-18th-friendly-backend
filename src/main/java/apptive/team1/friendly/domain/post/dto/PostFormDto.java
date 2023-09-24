package apptive.team1.friendly.domain.post.dto;

import apptive.team1.friendly.domain.post.entity.HashTag;
import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.post.entity.AudioGuide;
import apptive.team1.friendly.domain.post.vo.Coordinate;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class PostFormDto {

    /**
     * 새로 글 쓰는 경우 생성자. postId가 없음
     */
    @Builder
    public PostFormDto(String title, Set<HashTag> hashTags, int maxPeople, String description,
                       LocalDate startDate, LocalDate endDate, String location,
                       Set<Coordinate> coordinates, Set<String> rules) {
        this.title = title;
        this.maxPeople = maxPeople;
        this.description = description;
//        this.promiseTime = promiseTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.coordinates = coordinates;
        this.location = location;
        this.hashTags.addAll(hashTags);
        this.rules.addAll(rules);
    }

    private String title;

    private Set<HashTag> hashTags = new HashSet<>();

    private int maxPeople;

    private String description;

//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
//    private LocalDateTime promiseTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private Set<Coordinate> coordinates = new HashSet<>();

    private String location;

    private Set<String> rules = new HashSet<>();

    public static PostFormDto createPostFormDto(Post post) {
        return PostFormDto.builder()
                .rules(post.getRules())
                .title(post.getTitle())
                .hashTags(post.getHashTags())
                .description(post.getDescription())
                .maxPeople(post.getMaxPeople())
//                .promiseTime(post.getPromiseTime())
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .location(post.getLocation())
                .coordinates(post.getCoordinates())
                .build();
    }
}
