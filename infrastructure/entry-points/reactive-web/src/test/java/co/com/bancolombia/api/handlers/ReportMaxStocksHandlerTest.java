package co.com.bancolombia.api.handlers;

import co.com.bancolombia.model.productmodel.ProductModel;
import co.com.bancolombia.model.reportmaxstocksmodel.BranchWithMaxStockProductModel;
import co.com.bancolombia.model.reportmaxstocksmodel.ReportMaxStocksModel;
import co.com.bancolombia.usecase.reportmaxstocks.ReportMaxStocksUseCase;
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

class ReportMaxStocksHandlerTest {

    @Mock
    private ReportMaxStocksUseCase reportMaxStocksUseCase;
    @InjectMocks
    private ReportMaxStocksHandler reportMaxStocksHandler;

    @BeforeEach
    void setUp() {
        reportMaxStocksUseCase = Mockito.mock(ReportMaxStocksUseCase.class);
        reportMaxStocksHandler = new ReportMaxStocksHandler(reportMaxStocksUseCase);
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

        ReportMaxStocksModel franchise = ReportMaxStocksModel.builder()
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

        when(reportMaxStocksUseCase.findMaxStockProductsPerEachBranchByFranchiseId(franchiseIdInt)).thenReturn(Mono.just(franchise));
        create(reportMaxStocksHandler.listenGETFranchiseMaxStockProductUseCase(request))
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

        create(reportMaxStocksHandler.listenGETFranchiseMaxStockProductUseCase(request))
                .expectSubscription()
                .expectNextMatches(response -> {
                    Assertions.assertEquals(HttpStatusCode.valueOf(400),response.statusCode());
                    return true;
                }).expectComplete().verify();
    }
}