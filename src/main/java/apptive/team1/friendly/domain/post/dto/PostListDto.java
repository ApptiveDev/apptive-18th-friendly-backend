package apptive.team1.friendly.domain.post.dto;

import apptive.team1.friendly.domain.post.entity.HashTag;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
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
//    private String originalFileName;    // 원본 파일 이름
//    private String uploadFileName;      // 업로드 파일 이름
//    private String uploadFilePath;      // 업로드 파일 경로
//    private String uploadFileUrl;       // 업로드 파일 url

}
