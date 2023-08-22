package apptive.team1.friendly.domain.tourismboard.controller;

import apptive.team1.friendly.domain.tourismboard.dto.TourismDto;
import apptive.team1.friendly.domain.tourismboard.dto.TourismListDto;
import apptive.team1.friendly.domain.tourismboard.entity.FamousRestaurant;
import apptive.team1.friendly.domain.tourismboard.entity.ThemeTourism;
import apptive.team1.friendly.domain.tourismboard.entity.Tourism;
import apptive.team1.friendly.domain.tourismboard.entity.WalkingTourism;
import apptive.team1.friendly.domain.tourismboard.service.TourismService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TourismBoardController extends ApiBase {

    private final TourismService tourismService;

    @GetMapping("/tourism")
    public ResponseEntity<List<TourismListDto>> getTourismList(@RequestParam int pageNum, @RequestParam(required = false) String tag) {
        List<TourismListDto> tourismList = tourismService.getTourismList(pageNum, tag);
        return new ResponseEntity<>(tourismList, HttpStatus.OK);
    }

    @GetMapping("/tourism/{tourismId}")
    public ResponseEntity<TourismDto> getTourismDetail(@PathVariable Long tourismId) {
        TourismDto tourismDetail = tourismService.getTourismDetail(tourismId);
        return new ResponseEntity<>(tourismDetail, HttpStatus.OK);
    }

    private void saveWalkingTravel(int pageNo) throws URISyntaxException, JsonProcessingException {
        URI uri = new URI("https://apis.data.go.kr/6260000/WalkingService/getWalkingKr" +
                "?serviceKey=" + getApikey() + "&numOfRows=50&pageNo=" + pageNo + "&resultType=json");

        String tourismData = getTourismData(uri);

        ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(tourismData).get("getWalkingKr").get("item");
        if(jsonNode.size() == 0) {
            return;
        }
        List<WalkingTourism> walkingTourisms = objectMapper.readValue(jsonNode.toString(), new TypeReference<List<WalkingTourism>>() {});

        for (WalkingTourism walkingTourism : walkingTourisms) {
            walkingTourism.setTourismType();
            tourismService.saveTourism(walkingTourism);
        }
        saveWalkingTravel(pageNo+1);
    }

    private static String getTourismData(URI uri) {
        WebClient webClient = WebClientUtils.getWebClient();
        String data = webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return data;
    }

    private void saveThemeTravel(int pageNo) throws URISyntaxException, JsonProcessingException {
        URI uri = new URI("http://apis.data.go.kr/6260000/RecommendedService/getRecommendedKr" +
                "?serviceKey=" + getApikey() + "&numOfRows=50&pageNo=" + pageNo + "&resultType=json");

        String tourismData = getTourismData(uri);

        ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(tourismData).get("getRecommendedKr").get("item");
        if(jsonNode.size() == 0) {
            return;
        }
        List<ThemeTourism> themeTourisms = objectMapper.readValue(jsonNode.toString(), new TypeReference<List<ThemeTourism>>() {});
        for (ThemeTourism themeTourism : themeTourisms) {
                themeTourism.setTourismType();
                tourismService.saveTourism(themeTourism);
        }
        saveThemeTravel(pageNo+1);
    }

    private void saveFamousRestaurant(int pageNo) throws URISyntaxException, JsonProcessingException {
        URI uri = new URI("http://apis.data.go.kr/6260000/FoodService/getFoodKr" +
                "?serviceKey=" + getApikey() + "&numOfRows=50&pageNo=" + pageNo + "&resultType=json");

        String tourismData = getTourismData(uri);

        ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(tourismData).get("getFoodKr").get("item");
        if(jsonNode.size() == 0) {
            return;
        }
        List<FamousRestaurant> famousRestaurants = objectMapper.readValue(jsonNode.toString(), new TypeReference<List<FamousRestaurant>>() {});
        for (FamousRestaurant famousRestaurant : famousRestaurants) {
            famousRestaurant.setTourismType();
            tourismService.saveTourism(famousRestaurant);
        }
        saveFamousRestaurant(pageNo+1);
    }

    @GetMapping("tourism/admin")
    public void SetTourismData() throws URISyntaxException, JsonProcessingException {
//        int deletedCount = tourismService.deleteAll();
//        System.out.println("deleted:" + deletedCount);
//        saveWalkingTravel(1);
//        saveThemeTravel(1);
//        saveFamousRestaurant(1);
    }
}
