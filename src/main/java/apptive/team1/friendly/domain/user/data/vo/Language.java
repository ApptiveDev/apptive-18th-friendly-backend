package apptive.team1.friendly.domain.user.data.vo;

import apptive.team1.friendly.domain.user.data.constant.LanguageLevel;
import lombok.Getter;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
public class Language {

    private String languageName;

    @Enumerated(EnumType.STRING)
    private LanguageLevel languageLevel;
}
