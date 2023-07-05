package apptive.team1.friendly.domain.post.dto;

import apptive.team1.friendly.domain.user.data.dto.profile.ProfileImgDto;
import apptive.team1.friendly.domain.post.entity.HashTag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
public class PostListDto {

    private PostImageDto postImageDto;

    private String title;

    private int maxPeople;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime promiseTime;

    private String location;

    private String description;

    private Set<HashTag> hashTags = new HashSet<>();

    @Builder
    public PostListDto(PostImageDto postImageDto, String title, int maxPeople, LocalDateTime promiseTime, String location, String description, Set<HashTag> hashTags) {
        this.postImageDto = postImageDto;
        this.title = title;
        this.maxPeople = maxPeople;
        this.promiseTime = promiseTime;
        this.location = location;
        this.description = description;
        HashSet<HashTag> copyHashTags = new HashSet<>();
        for(HashTag hashTag : hashTags) {
            copyHashTags.add(hashTag);
        }
        this.hashTags = copyHashTags;
    }

}
