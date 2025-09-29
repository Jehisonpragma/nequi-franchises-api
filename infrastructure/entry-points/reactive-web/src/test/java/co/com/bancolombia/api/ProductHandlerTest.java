package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.RequestCreateProductDto;
import co.com.bancolombia.api.handlers.ProductHandler;
import co.com.bancolombia.model.productmodel.ProductModel;
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

import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

class ProductHandlerTest {

    @Mock
    private ProductUseCase productUseCase;
    @InjectMocks
    private ProductHandler productHandler;

    @BeforeEach
    void setUp() {
        productUseCase = Mockito.mock(ProductUseCase.class);
        productHandler = new ProductHandler(productUseCase);
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
        create(productHandler.listenPOSTProductUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(200),response.statusCode());
            return true;
        }).expectComplete().verify();
    }

    @Test
    void listenPATCHProductNameUseCase() {
        String productId = "1";
        Integer productIdInt = 1;
        String name = "product";

        ProductModel productModel = ProductModel.builder().productId(productIdInt).name(name).build();

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/api/product/name"))
                .header("X-Test", "123")
                .queryParam("id",productId)
                .queryParam("name",name)
                .build();

        when(productUseCase.updateProductName(productIdInt,name)).thenReturn(Mono.just(productModel));
        create(productHandler.listenPATCHProductNameUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(200),response.statusCode());
            return true;
        }).expectComplete().verify();
    }

    @Test
    void listenPATCHProductNameUseCaseWithBadRequest() {

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/api/product/name"))
                .header("X-Test", "123")
                .build();

        create(productHandler.listenPATCHProductNameUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(400),response.statusCode());
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
        create(productHandler.listenDELETEProductUseCase(request)).expectSubscription().expectNextMatches(response -> {
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

        create(productHandler.listenDELETEProductUseCase(request)).expectSubscription().expectNextMatches(response -> {
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
        create(productHandler.listenPATCHProductStockUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(200),response.statusCode());
            return true;
        }).expectComplete().verify();
    }

    @Test
    void listenPATCHProductStockUseCaseWithBadRequestError() {
        String productId = "1";

        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.PATCH)
                .uri(URI.create("/api/product/stock"))
                .header("X-Test", "123")
                .queryParam("id",productId)
                .build();

        create(productHandler.listenPATCHProductStockUseCase(request)).expectSubscription().expectNextMatches(response -> {
            Assertions.assertEquals(HttpStatusCode.valueOf(400),response.statusCode());
            return true;
        }).expectComplete().verify();
    }

}