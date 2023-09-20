package apptive.team1.friendly.domain.tourismboard.controller;

import apptive.team1.friendly.domain.tourismboard.dto.TourismDto;
import apptive.team1.friendly.domain.tourismboard.dto.TourismListDto;
import apptive.team1.friendly.domain.tourismboard.service.TourismService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TourismBoardController {

    private final TourismService tourismService;

    @GetMapping("/tourism")
    public ResponseEntity<List<TourismListDto>> TourismList(@RequestParam int pageNum, @RequestParam(required = false) String tag) {
        List<TourismListDto> tourismList = tourismService.TourismList(pageNum, tag);
        return new ResponseEntity<>(tourismList, HttpStatus.OK);
    }

    @GetMapping("/tourism/noPaging")
    public ResponseEntity<List<TourismListDto>> TourismListNoPagingVersion(@RequestParam(required = false) String tag) {
        List<TourismListDto> tourismList = tourismService.TourismListNoPagingVersion(tag);
        return new ResponseEntity<>(tourismList, HttpStatus.OK);
    }

    @GetMapping("/tourism/{tourismId}")
    public ResponseEntity<TourismDto> TourismDetail(@PathVariable Long tourismId) {
        TourismDto tourismDetail = tourismService.TourismDetail(tourismId);
        return new ResponseEntity<>(tourismDetail, HttpStatus.OK);
    }
}
