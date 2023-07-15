package apptive.team1.friendly.domain.tourismboard.controller;

import apptive.team1.friendly.domain.post.vo.AudioGuide;
import apptive.team1.friendly.domain.tourismboard.vo.WalkingTourInfo;
import apptive.team1.friendly.global.baseEntity.ApiBase;
import apptive.team1.friendly.global.config.ObjectMapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TourismBoardController extends ApiBase {

    public Mono<List<WalkingTourInfo>> getWalkingTravel() throws URISyntaxException {
        WebClient webClient = WebClient.create();
        URI uri = new URI("http://apis.data.go.kr/6260000/WalkingService/getWalkingKr" +
                "?serviceKey=" + getApikey() + "&numOfRows=10&pageNo=1&resultType=json");

        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(jsonResponse -> {
                    JsonNode jsonNode;
                    ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();
                    try{
                        jsonNode = objectMapper.readTree(jsonResponse).get("getWalkingKr").get("item");
                        List<WalkingTourInfo> walkingTourInfos = objectMapper.readValue(jsonNode.toString(), new TypeReference<List<WalkingTourInfo>>() {
                        });
                        return Mono.just(walkingTourInfos);
                    } catch(JsonProcessingException e) {
                        return Mono.error(e);
                    }
                })
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(new ArrayList<>());
                });
    }


}
