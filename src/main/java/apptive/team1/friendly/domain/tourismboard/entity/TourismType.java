package apptive.team1.friendly.domain.tourismboard.entity;

public enum TourismType {
    WALKING, THEME, RESTAURANT;

    public static String toKoreanName(TourismType tourismType) {
        switch(tourismType) {
            case WALKING:   return "도보여행";
            case THEME: return "테마여행";
            case RESTAURANT:    return "맛집";
        }
        return "";
    }
}
