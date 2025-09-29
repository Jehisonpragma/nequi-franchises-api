package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.RequestCreateFranchiseDto;
import co.com.bancolombia.api.handlers.FranchiseHandler;
import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.usecase.franchise.FranchiseUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.mockito.Mockito.when;

import static reactor.test.StepVerifier.create;

class FranchiseHandlerTest {

    @Mock
    private FranchiseUseCase franchiseUseCase;
    @InjectMocks
    private FranchiseHandler franchiseHandler;

    @BeforeEach
    void setUp() {
        franchiseUseCase = Mockito.mock(FranchiseUseCase.class);
        franchiseHandler = new FranchiseHandler(franchiseUseCase);
    }

    @Test
    void listenPOSTFranchiseUseCase() {
        String franchiseName = "Franchise";
        FranchiseModel franchiseModel = FranchiseModel.builder().franchiseId(1).name(franchiseName).build();
        RequestCreateFranchiseDto requestCreateFranchiseDto = new RequestCreateFranchiseDto(franchiseName);

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .uri(URI.create("/api/franchise"))
                .header("X-Test", "123")
                .body(Mono.just(requestCreateFranchiseDto));

        when(franchiseUseCase.createFranchise(franchiseName)).thenReturn(Mono.just(franchiseModel));
        create(franchiseHandler.listenPOSTFranchiseUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(200),response.statusCode());
            return true;
        }).expectComplete().verify();
    }

    @Test
    void listenPOSTFranchiseUseCaseWithInternalError() {
        String franchiseName = "Franchise";
        FranchiseModel franchiseModel = FranchiseModel.builder().build();

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .uri(URI.create("/api/franchise"))
                .header("X-Test", "123")
                .body(Mono.just("other text"));

        when(franchiseUseCase.createFranchise(franchiseName)).thenReturn(Mono.just(franchiseModel));
        create(franchiseHandler.listenPOSTFranchiseUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(500),response.statusCode());
            return true;
        }).expectComplete().verify();
    }

    @Test
    void listenPATCHFranchiseNameUseCase() {
        String franchiseId = "1";
        Integer franchiseIdInt = 1;
        String name = "franchise";

        FranchiseModel franchiseModel = FranchiseModel.builder().franchiseId(franchiseIdInt).name(name).build();

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/api/franchise/name"))
                .header("X-Test", "123")
                .queryParam("id",franchiseId)
                .queryParam("name",name)
                .build();

        when(franchiseUseCase.updateFranchiseName(franchiseIdInt,name)).thenReturn(Mono.just(franchiseModel));
        create(franchiseHandler.listenPATCHFranchiseNameUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(200),response.statusCode());
            return true;
        }).expectComplete().verify();
    }

    @Test
    void listenPATCHFranchiseNameUseCaseWithBadRequest() {

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/api/franchise/name"))
                .header("X-Test", "123")
                .build();

        create(franchiseHandler.listenPATCHFranchiseNameUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(400),response.statusCode());
            return true;
        }).expectComplete().verify();
    }

}