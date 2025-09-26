package co.com.bancolombia.api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

class SecurityHeadersConfigTest {

    private SecurityHeadersConfig securityHeadersConfig;

    ServerWebExchange exchange;
    @Mock
    WebFilterChain chain;


    @BeforeEach
    void setUp() {
        exchange  = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                        .header("X-Test-Header", "123")
                        .build()
        );

        chain= Mockito.mock(WebFilterChain.class);

        securityHeadersConfig = new SecurityHeadersConfig();
    }

    @Test
    void filter() {
        when(chain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());
        create(securityHeadersConfig.filter(exchange,chain))
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();

    }
}