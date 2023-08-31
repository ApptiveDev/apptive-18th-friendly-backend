package apptive.team1.friendly.domain.audioguide.controller;

import apptive.team1.friendly.domain.post.entity.AudioGuide;
import apptive.team1.friendly.domain.audioguide.repository.AudioGuideRepository;
import apptive.team1.friendly.global.baseEntity.ApiBase;
import apptive.team1.friendly.global.utils.ObjectMapperUtils;
import apptive.team1.friendly.global.utils.WebClientUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AudioGuideController extends ApiBase {

    private final AudioGuideRepository audioGuideRepository;

    @GetMapping("/audioGuide")
    public ResponseEntity<List<AudioGuide>> audioGuideList(@RequestParam int pageNum, @RequestParam(required = false) String keyword) {
        if(keyword != null && keyword.length() < 2) {
            throw new RuntimeException("검색어를 두 글자 이상 입력해주세요.");
        }
        List<AudioGuide> audioGuides = audioGuideRepository.findAudioGuides(pageNum, keyword);
        return new ResponseEntity<>(audioGuides, HttpStatus.OK);
    }

    @GetMapping("/audioGuideNoPaging")
    public ResponseEntity<List<AudioGuide>> audioGuideListNoPage(@RequestParam(required = false) String keyword) {
        if(keyword != null && keyword.length() < 2) {
            throw new RuntimeException("검색어를 두 글자 이상 입력해주세요.");
        }
        List<AudioGuide> audioGuides = audioGuideRepository.findAudioGuidesNoPaging(keyword);
        return new ResponseEntity<>(audioGuides, HttpStatus.OK);
    }


    /**
     * 오디오 가이드 API
     */
    @GetMapping("/audioGuide/admin/9093")
    public ResponseEntity<Void> saveAudioGuides(@RequestParam("languageCode") String languageCode) throws URISyntaxException, JsonProcessingException {
        getAudioGuides(languageCode, 0);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void getAudioGuides(String languageCode, int pageNum) throws URISyntaxException, JsonProcessingException{
        URI uri = new URI("https://apis.data.go.kr/B551011/Odii/storyBasedList" +
                "?MobileOS=" + getMobileOS() + "&MobileApp=Photour" +
                "&serviceKey=" + getApikey() +
                "&_type=json" +
                "&langCode=" + languageCode +
                "&pageNo=" + pageNum
        );

        WebClient webClient = WebClientUtils.getWebClient();

        String data = webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(data).get("response").get("body").get("items").get("item");
        if(jsonNode == null || jsonNode.size() == 0)
            return;
        List<AudioGuide> audioGuides = objectMapper.readValue(jsonNode.toString(), new TypeReference<List<AudioGuide>>() {});
        for (AudioGuide audioGuide : audioGuides) {
            if(!audioGuide.getAudioUrl().equals(""))
                audioGuideRepository.save(audioGuide);
        }
        getAudioGuides(languageCode, pageNum+1);
    }
}
