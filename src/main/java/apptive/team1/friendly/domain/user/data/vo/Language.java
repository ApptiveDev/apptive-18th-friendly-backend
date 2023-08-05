package apptive.team1.friendly.domain.user.data.vo;

import apptive.team1.friendly.domain.user.data.constant.LanguageLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Language {

    private String languageName;

    @Enumerated(EnumType.STRING)
    private LanguageLevel languageLevel;
}
