package apptive.team1.friendly.domain.post.dto;

import apptive.team1.friendly.domain.post.entity.HashTag;
import apptive.team1.friendly.global.common.s3.FileInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class PostListDto {
    // 게시판 리스트에 보이는 필드
    private Long postId;

    private String title;

    private int maxPeople;

    private LocalDateTime promiseTime;

    private String location;

    private List<HashTag> hashTag = new ArrayList<HashTag>();

    // 이미지 필드
    private FileInfo fileInfo;

}
