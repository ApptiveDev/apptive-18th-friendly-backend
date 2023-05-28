package apptive.team1.friendly.domain.post.dto;

import apptive.team1.friendly.domain.post.entity.HashTag;
import apptive.team1.friendly.global.common.s3.FileInfo;
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
public class PostFormDto {

    /**
     * 새로 글 쓰는 경우 생성자. postId가 없음
     */
    public PostFormDto(String title, List<HashTag> hashTag, int maxPeople, String description, LocalDateTime promiseTime, String location, List<String> rules) {
        this.title = title;
        this.hashTag = hashTag;
        this.maxPeople = maxPeople;
        this.description = description;
        this.promiseTime = promiseTime;
        this.location = location;
        this.rules = rules;
    }

//    /**
//     * update할 때 생성자. postId를 body로 받음
//     */
//    public PostFormDto(Long postId, String title, List<HashTag> hashTag, int maxPeople, String description, LocalDateTime promiseTime, String location, List<String> rules) {
//        this.postId = postId;
//        this.title = title;
//        this.hashTag = hashTag;
//        this.maxPeople = maxPeople;
//        this.description = description;
//        this.promiseTime = promiseTime;
//        this.location = location;
//        this.rules = rules;
//    }

//    private Long postId; // update 할 때만 사용

    private String title;

    private List<HashTag> hashTag = new ArrayList<HashTag>();

    private int maxPeople;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime promiseTime;

    private String location;

    private List<String> rules = new ArrayList<String>();

    // 이미지 필드
    private FileInfo fileInfo;
}
