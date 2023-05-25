package apptive.team1.friendly.domain.post.dto;

import apptive.team1.friendly.common.s3.FileInfo;
import apptive.team1.friendly.domain.post.entity.HashTag;
import apptive.team1.friendly.domain.user.data.dto.AccountInfoResponse;
import apptive.team1.friendly.domain.user.data.entity.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class PostDto {

    public PostDto(Long id, AccountInfoResponse accountInfo, String title, List<HashTag> hashTag, int maxPeople, String description, LocalDateTime promiseTime, String location, List<String> rules) {
        this.accountInfo = accountInfo;
        this.postId = id;
        this.title = title;
        this.hashTag = hashTag;
        this.maxPeople = maxPeople;
        this.description = description;
        this.promiseTime = promiseTime;
        this.location = location;
        this.rules = rules;
    }
    private Long postId;

    private AccountInfoResponse accountInfo;

    private String title;

    private List<HashTag> hashTag = new ArrayList<HashTag>();

    private int maxPeople;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime promiseTime;

    private String location;

    private List<String> rules = new ArrayList<String>();

}
