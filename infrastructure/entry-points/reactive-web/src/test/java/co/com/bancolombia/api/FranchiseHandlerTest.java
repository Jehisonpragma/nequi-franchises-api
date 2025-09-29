package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.RequestCreateFranchiseDto;
import co.com.bancolombia.api.handlers.FranchiseHandler;
import co.com.bancolombia.model.branchmodel.BranchWithMaxStockProductModel;
import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.model.franchisemodel.FranchiseWithMaxStockProductsModel;
import co.com.bancolombia.model.productmodel.ProductModel;
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
import java.util.List;

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

    @Test
    void listenGETFranchiseMaxStockProductUseCase() {
        String franchiseId = "1";
        Integer franchiseIdInt = 1;

        ProductModel productMaxInBranch1 = ProductModel.builder().productId(1).branchId(1).name("product1").stock(200).build();
        ProductModel productMaxInBranch2 = ProductModel.builder().productId(2).branchId(2).name("product2").stock(50).build();

        BranchWithMaxStockProductModel branch1 = BranchWithMaxStockProductModel.builder()
                .branchId(1)
                .name("branch1")
                .maxStockProduct(productMaxInBranch1)
                .build();

        BranchWithMaxStockProductModel branch2 = BranchWithMaxStockProductModel.builder()
                .branchId(2)
                .name("branch2")
                .maxStockProduct(productMaxInBranch2)
                .build();

        FranchiseWithMaxStockProductsModel franchise = FranchiseWithMaxStockProductsModel.builder()
                .franchiseId(franchiseIdInt)
                .name("franchise")
                .branches(List.of(branch1,branch2))
                .build();

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.GET)
                .uri(URI.create("/api/franchise/branches/products/max-stock"))
                .header("X-Test", "123")
                .queryParam("franchise_id", franchiseId)
                .build();

        when(franchiseUseCase.findMaxStockProductsPerEachBranchByFranchiseId(franchiseIdInt)).thenReturn(Mono.just(franchise));
        create(franchiseHandler.listenGETFranchiseMaxStockProductUseCase(request))
            .expectSubscription()
            .expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(200),response.statusCode());
            return true;
        }).expectComplete().verify();
    }

    @Test
    void listenGETFranchiseMaxStockProductUseCaseWithBadRequestError() {
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.GET)
                .uri(URI.create("/api/franchise/branches/products/max-stock"))
                .header("X-Test", "123")
                .build();

        create(franchiseHandler.listenGETFranchiseMaxStockProductUseCase(request))
                .expectSubscription()
                .expectNextMatches(response -> {
                    Assertions.assertEquals(HttpStatusCode.valueOf(400),response.statusCode());
                    return true;
                }).expectComplete().verify();
    }
}