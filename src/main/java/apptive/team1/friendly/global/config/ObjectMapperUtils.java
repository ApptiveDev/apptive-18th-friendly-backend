package apptive.team1.friendly.global.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtils {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 객체에 json의 필드가 없어도 매핑 가능하도록 설정
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
