package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.RequestCreateBranchDto;
import co.com.bancolombia.api.dto.RequestCreateFranchiseDto;
import co.com.bancolombia.api.dto.RequestCreateProductDto;
import co.com.bancolombia.model.branchmodel.BranchModel;
import co.com.bancolombia.model.branchmodel.BranchWithMaxStockProductModel;
import co.com.bancolombia.model.exceptionmodel.BusinessException;
import co.com.bancolombia.model.exceptionmodel.ErrorCode;
import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.model.franchisemodel.FranchiseWithMaxStockProductsModel;
import co.com.bancolombia.model.productmodel.ProductModel;
import co.com.bancolombia.usecase.branch.BranchUseCase;
import co.com.bancolombia.usecase.franchise.FranchiseUseCase;
import co.com.bancolombia.usecase.product.ProductUseCase;
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

class HandlerTest {

    @Mock
    private FranchiseUseCase franchiseUseCase;
    @Mock
    private BranchUseCase branchUseCase;
    @Mock
    private ProductUseCase productUseCase;
    @InjectMocks
    private Handler handler;

    @BeforeEach
    void setUp() {
        franchiseUseCase = Mockito.mock(FranchiseUseCase.class);
        branchUseCase = Mockito.mock(BranchUseCase.class);
        productUseCase = Mockito.mock(ProductUseCase.class);
        handler = new Handler(franchiseUseCase,branchUseCase,productUseCase);
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
    void listenPOSTFranchiseUseCaseWithInternalError() {
        String franchiseName = "Franchise";
        FranchiseModel franchiseModel = FranchiseModel.builder().build();
        RequestCreateFranchiseDto requestCreateFranchiseDto = new RequestCreateFranchiseDto(franchiseName);

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .uri(URI.create("/api/franchise"))
                .header("X-Test", "123")
                .body(Mono.just("other text"));

        when(franchiseUseCase.createFranchise(franchiseName)).thenReturn(Mono.just(franchiseModel));
        create(handler.listenPOSTFranchiseUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(500),response.statusCode());
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
        create(handler.listenPOSTBranchUseCase(request)).expectSubscription().expectNextMatches(response -> {
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
        create(handler.listenPOSTBranchUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(400),response.statusCode());
            return true;
        }).expectComplete().verify();
    }

    @Test
    void listenPOSTProductUseCase() {
        String productName = "Product";
        Integer branchId = 1;
        Integer stock = 1;
        ProductModel productModel = ProductModel.builder().productId(1).branchId(branchId).stock(stock).name(productName).build();
        RequestCreateProductDto requestCreateProductDto = new RequestCreateProductDto(productName,branchId,stock);

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .uri(URI.create("/api/product"))
                .header("X-Test", "123")
                .body(Mono.just(requestCreateProductDto));

        when(productUseCase.createProduct(productName,branchId,stock)).thenReturn(Mono.just(productModel));
        create(handler.listenPOSTProductUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(200),response.statusCode());
            return true;
        }).expectComplete().verify();
    }

    @Test
    void listenDELETEProductUseCase() {
        String productId = "1";
        Integer productIdInt = 1;
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.DELETE)
                .uri(URI.create("/api/product"))
                .header("X-Test", "123")
                .queryParam("id",productId).build();

        when(productUseCase.deleteProduct(productIdInt)).thenReturn(Mono.just(Boolean.TRUE));
        create(handler.listenDELETEProductUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(200),response.statusCode());
            return true;
        }).expectComplete().verify();
    }

    @Test
    void listenDELETEProductUseCaseWithBadRequestError() {
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.DELETE)
                .uri(URI.create("/api/product"))
                .header("X-Test", "123").build();

        create(handler.listenDELETEProductUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(400),response.statusCode());
            return true;
        }).expectComplete().verify();
    }

    @Test
    void listenPATCHProductStockUseCase() {
        String productId = "1";
        String stock = "20";
        Integer productIdInt = 1;
        Integer stockInt = 20;
        Integer branchId = 2;
        String productName = "product1";

        ProductModel productModel = ProductModel.builder().productId(1).branchId(branchId).stock(stockInt).name(productName).build();

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/api/product/stock"))
                .header("X-Test", "123")
                .queryParam("id",productId)
                .queryParam("stock",stock)
                .build();

        when(productUseCase.modifyStockInProduct(productIdInt,stockInt)).thenReturn(Mono.just(productModel));
        create(handler.listenPATCHProductStockUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(200),response.statusCode());
            return true;
        }).expectComplete().verify();
    }

    @Test
    void listenPATCHProductStockUseCaseWithBadRequestErrror() {
        String productId = "1";

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/api/product/stock"))
                .header("X-Test", "123")
                .queryParam("id",productId)
                .build();

        create(handler.listenPATCHProductStockUseCase(request)).expectSubscription().expectNextMatches(response -> {
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
        create(handler.listenGETFranchiseMaxStockProductUseCase(request))
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

        create(handler.listenGETFranchiseMaxStockProductUseCase(request))
                .expectSubscription()
                .expectNextMatches(response -> {
                    Assertions.assertEquals(HttpStatusCode.valueOf(400),response.statusCode());
                    return true;
                }).expectComplete().verify();
    }
}