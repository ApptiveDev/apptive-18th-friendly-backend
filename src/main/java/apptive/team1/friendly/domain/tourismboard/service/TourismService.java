package apptive.team1.friendly.domain.tourismboard.service;

import apptive.team1.friendly.domain.tourismboard.dto.TourismDto;
import apptive.team1.friendly.domain.tourismboard.dto.TourismListDto;
import apptive.team1.friendly.domain.tourismboard.entity.FamousRestaurant;
import apptive.team1.friendly.domain.tourismboard.entity.ThemeTourism;
import apptive.team1.friendly.domain.tourismboard.entity.Tourism;
import apptive.team1.friendly.domain.tourismboard.entity.WalkingTourism;
import apptive.team1.friendly.domain.tourismboard.repository.TourismRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TourismService {

    private final TourismRepository tourismRepository;

    @Transactional
    public void saveTourism(Tourism tourism) {
        tourismRepository.save(tourism);
    }

    @Transactional
    public int deleteAll() {
        return tourismRepository.deleteAll();
    }

    public List<TourismListDto> getTourismList(int pageNum, String tag) {
        List<Tourism> tourismList = tourismRepository.getTourismList(pageNum, tag);
        return TourismListDto.create(tourismList);
    }

    public TourismDto getTourismDetail(Long tourismId) {
        Tourism tourism = tourismRepository.findOneById(tourismId);
        return TourismDto.create(tourism);
    }
}
