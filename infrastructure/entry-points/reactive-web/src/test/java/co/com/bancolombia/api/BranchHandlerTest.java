package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.RequestCreateBranchDto;
import co.com.bancolombia.api.handlers.BranchHandler;
import co.com.bancolombia.model.branchmodel.BranchModel;
import co.com.bancolombia.model.exceptionmodel.BusinessException;
import co.com.bancolombia.model.exceptionmodel.ErrorCode;
import co.com.bancolombia.usecase.branch.BranchUseCase;
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

class BranchHandlerTest {

    @Mock
    private BranchUseCase branchUseCase;
    @InjectMocks
    private BranchHandler branchHandler;

    @BeforeEach
    void setUp() {
        branchUseCase = Mockito.mock(BranchUseCase.class);
        branchHandler = new BranchHandler(branchUseCase);
    }

    @Test
    void listenPOSTBranchUseCase() {
        String branchName = "Branch";
        Integer franchiseId = 1;
        BranchModel branchModel = BranchModel.builder().branchId(1).franchiseId(franchiseId).name(branchName).build();
        RequestCreateBranchDto requestCreateBranchDto = new RequestCreateBranchDto(branchName,franchiseId);

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .uri(URI.create("/api/branch"))
                .header("X-Test", "123")
                .body(Mono.just(requestCreateBranchDto));

        when(branchUseCase.createBranch(branchName,franchiseId)).thenReturn(Mono.just(branchModel));
        create(branchHandler.listenPOSTBranchUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(200),response.statusCode());
            return true;
        }).expectComplete().verify();
    }

    @Test
    void listenPATCHBranchNameUseCase() {
        String branchId = "1";
        Integer branchIdInt = 1;
        String name = "branch";

        BranchModel branchModel = BranchModel.builder().branchId(branchIdInt).name(name).build();

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/api/branch/name"))
                .header("X-Test", "123")
                .queryParam("id",branchId)
                .queryParam("name",name)
                .build();

        when(branchUseCase.updateBranchName(branchIdInt,name)).thenReturn(Mono.just(branchModel));
        create(branchHandler.listenPATCHBranchNameUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(200),response.statusCode());
            return true;
        }).expectComplete().verify();
    }

    @Test
    void listenPATCHBranchNameUseCaseWithBadRequest() {

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/api/branch/name"))
                .header("X-Test", "123")
                .build();

        create(branchHandler.listenPATCHBranchNameUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(400),response.statusCode());
            return true;
        }).expectComplete().verify();
    }

    @Test
    void listenPOSTBranchUseCaseWithBusinessError() {
        String branchName = "Branch";
        Integer franchiseId = 1;
        RequestCreateBranchDto requestCreateBranchDto = new RequestCreateBranchDto(branchName,franchiseId);

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .uri(URI.create("/api/branch"))
                .header("X-Test", "123")
                .body(Mono.just(requestCreateBranchDto));

        when(branchUseCase.createBranch(branchName,franchiseId)).thenReturn(Mono.error(new BusinessException(ErrorCode.E422000)));
        create(branchHandler.listenPOSTBranchUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(422),response.statusCode());
            return true;
        }).expectComplete().verify();
    }

    @Test
    void listenPOSTBranchUseCaseWithBadRequestError() {
        String branchName = "Branch";
        Integer franchiseId = 1;
        RequestCreateBranchDto requestCreateBranchDto = new RequestCreateBranchDto(branchName,franchiseId);

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .uri(URI.create("/api/branch"))
                .header("X-Test", "123")
                .body(Mono.just(requestCreateBranchDto));

        when(branchUseCase.createBranch(branchName,franchiseId)).thenReturn(Mono.error(new IllegalArgumentException()));
        create(branchHandler.listenPOSTBranchUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(400),response.statusCode());
            return true;
        }).expectComplete().verify();
    }

}