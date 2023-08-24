package apptive.team1.friendly.domain.user.data.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum LanguageLevel {
    NATIVE("모국어"),
    BEGINNER("초급자"),
    INTERMEDIATE("중급자"),
    ADVANCED("상급자");

    private final String name;
    private static final Map<String, LanguageLevel> levelByNameMap = new HashMap<>();

    static {
        for (LanguageLevel level : LanguageLevel.values()) {
            levelByNameMap.put(level.getName(), level);
        }
    }

    LanguageLevel(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static LanguageLevel getLevelByName(String name) {
        return levelByNameMap.get(name);
    }
}