package apptive.team1.friendly.domain.user.data.vo;

import apptive.team1.friendly.domain.user.data.constant.LanguageLevel;
import apptive.team1.friendly.domain.user.data.converter.LanguageLevelConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;
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
    @Convert(converter = LanguageLevelConverter.class)
    private LanguageLevel languageLevel;
}
