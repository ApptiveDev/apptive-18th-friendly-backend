package apptive.team1.friendly.global.baseEntity;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;


@Getter
public class ApiBase {
    @Value("${travelAPI.encoding-key}")
    private String apikey;

    @Value("${travelAPI.mobileOS}")
    private String mobileOS;
}
