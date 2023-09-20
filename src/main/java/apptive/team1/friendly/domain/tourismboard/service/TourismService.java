package apptive.team1.friendly.domain.tourismboard.service;

import apptive.team1.friendly.domain.tourismboard.dto.TourismDto;
import apptive.team1.friendly.domain.tourismboard.dto.TourismListDto;
import apptive.team1.friendly.domain.tourismboard.entity.FamousRestaurant;
import apptive.team1.friendly.domain.tourismboard.entity.ThemeTourism;
import apptive.team1.friendly.domain.tourismboard.entity.Tourism;
import apptive.team1.friendly.domain.tourismboard.entity.WalkingTourism;
import apptive.team1.friendly.domain.tourismboard.exception.NoTourismException;
import apptive.team1.friendly.domain.tourismboard.repository.TourismRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public List<TourismListDto> TourismList(int pageNum, String tag) {
        List<Tourism> tourismList = tourismRepository.findTourismList(pageNum, tag);
        return TourismListDto.create(tourismList);
    }

    public List<TourismListDto> TourismListNoPagingVersion(String tag) {
        List<Tourism> tourismList = tourismRepository.findTourismListNoPagingVersion(tag);
        return TourismListDto.create(tourismList);
    }

    public TourismDto TourismDetail(Long tourismId) {
        Optional<Tourism> tourismOptional = tourismRepository.findOneById(tourismId);
        tourismOptional.orElseThrow(() -> new NoTourismException("존재하는 관광지가 없습니다."));
        return TourismDto.create(tourismOptional.get());
    }
}
