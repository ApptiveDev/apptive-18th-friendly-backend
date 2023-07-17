package apptive.team1.friendly.domain.tourismboard.service;

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

    public List<WalkingTourism> getWalkingTourism(int pageNum) {
        return tourismRepository.getWalkingTourism(pageNum);
    }

    public List<ThemeTourism> getThemeTourism(int pageNum) {
        return tourismRepository.getThemeTourism(pageNum);
    }

    public List<Tourism> getAllTourism(int pageNum) {
        return tourismRepository.getAllTourism(pageNum);
    }

    public List<FamousRestaurant> getFamousRestaurant(int pageNum) {
        return tourismRepository.getFamousRestaurant(pageNum);
    }

    @Transactional
    public void saveTourism(Tourism tourism) {
        tourismRepository.save(tourism);
    }

    @Transactional
    public int deleteAll() {
        return tourismRepository.deleteAll();
    }
}
