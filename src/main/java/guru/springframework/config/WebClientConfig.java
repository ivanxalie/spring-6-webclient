package guru.springframework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.spring.webflux.LogbookExchangeFilterFunction;

@Configuration
public class WebClientConfig implements WebClientCustomizer {
    private final String rootUrl;
    private final String clientName;
    private final ReactiveOAuth2AuthorizedClientManager manager;

    public WebClientConfig(
            @Value("${webclient.rootUrl}") String rootUrl,
            @Value("${spring.security.oauth2.client.registration.springauth.client-name}") String clientName,
            ReactiveOAuth2AuthorizedClientManager manager) {
        this.rootUrl = rootUrl;
        this.clientName = clientName;
        this.manager = manager;
    }

    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction function =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(manager);
        function.setDefaultClientRegistrationId(clientName);

        LogbookExchangeFilterFunction logbookExchangeFilterFunction =
                new LogbookExchangeFilterFunction(Logbook.create());

        webClientBuilder
                .filter(function)
                .filter(logbookExchangeFilterFunction)
                .baseUrl(rootUrl);
    }
}
