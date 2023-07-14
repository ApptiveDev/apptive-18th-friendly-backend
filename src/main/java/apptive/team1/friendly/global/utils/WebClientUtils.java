package apptive.team1.friendly.global.utils;

import org.springframework.web.reactive.function.client.WebClient;

public class WebClientUtils {
    private static final WebClient webClient;

    static {
        webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .build();
    }

    public static WebClient getWebClient() {
        return webClient;
    }

}
