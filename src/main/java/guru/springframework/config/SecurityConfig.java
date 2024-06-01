package guru.springframework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;

@Configuration
public class SecurityConfig {

    @Bean
    public ReactiveOAuth2AuthorizedClientManager manager(
            ReactiveClientRegistrationRepository repository,
            ReactiveOAuth2AuthorizedClientService service
    ) {
        ReactiveOAuth2AuthorizedClientProvider provider =
                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials().build();

        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(repository, service);

        manager.setAuthorizedClientProvider(provider);

        return manager;
    }
}
