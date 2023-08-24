package apptive.team1.friendly.domain.user.data.converter;

import apptive.team1.friendly.domain.user.data.constant.LanguageLevel;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LanguageLevelConverter implements AttributeConverter<LanguageLevel, String> {

    @Override
    public String convertToDatabaseColumn(LanguageLevel level) {
        return (level == null) ? null : level.getName();
    }

    @Override
    public LanguageLevel convertToEntityAttribute(String name) {
        return (name == null) ? null : LanguageLevel.getLevelByName(name);
    }
}

