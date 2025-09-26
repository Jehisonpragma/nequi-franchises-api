package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.RequestCreateBranchDto;
import co.com.bancolombia.api.dto.RequestCreateFranchiseDto;
import co.com.bancolombia.model.branchmodel.BranchModel;
import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.usecase.branch.BranchUseCase;
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

class HandlerTest {

    @Mock
    private FranchiseUseCase franchiseUseCase;
    @Mock
    private BranchUseCase branchUseCase;
    @InjectMocks
    private Handler handler;

    @BeforeEach
    void setUp() {
        franchiseUseCase = Mockito.mock(FranchiseUseCase.class);
        branchUseCase = Mockito.mock(BranchUseCase.class);
        handler = new Handler(franchiseUseCase,branchUseCase);
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
        create(handler.listenPOSTFranchiseUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(200),response.statusCode());
            return true;
        }).expectComplete().verify();
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
        create(handler.listenPOSTBranchUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(200),response.statusCode());
            return true;
        }).expectComplete().verify();
    }
}