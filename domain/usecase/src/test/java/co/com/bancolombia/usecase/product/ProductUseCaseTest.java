package co.com.bancolombia.usecase.product;

import co.com.bancolombia.model.productmodel.ProductModel;
import co.com.bancolombia.model.productmodel.gateways.ProductModelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductUseCaseTest {

    @Mock
    private ProductModelRepository productModelRepository;
    @InjectMocks
    private ProductUseCase productUseCase;

    @BeforeEach
    void setUp() {
        productModelRepository = mock(ProductModelRepository.class);
        productUseCase = new ProductUseCase(productModelRepository);
    }

    @Test
    void testCreateProduct() {
        String productName = "Product1";
        Integer branchId = 1;
        Integer stock = 20;

        ProductModel outcommingProductModel = ProductModel.builder().productId(1).branchId(branchId).name(productName).stock(stock).build();

        when(productModelRepository.saveProduct(any(ProductModel.class))).thenReturn(Mono.just(outcommingProductModel));

        Mono<ProductModel> result = productUseCase.createProduct(productName, branchId,stock);

        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(productModel ->
                        productModel.getName().equals(productName) &&
                        productModel.getBranchId().equals(branchId) &&
                        productModel.getStock().equals(stock)
                )
                .expectNextCount(0).expectComplete().verify();
    }


    @Test
    void testDeleteProduct() {
        Integer productId = 1;

        when(productModelRepository.deleteProductById(productId)).thenReturn(Mono.just(Boolean.TRUE));

        Mono<Boolean> result = productUseCase.deleteProduct(productId);

        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(response -> {
                    Assertions.assertTrue( response);
                    return true;
                }).expectComplete().verify();
    }

    @Test
    void testModifyStockInProduct() {
        String productName = "Product1";
        Integer productId = 1;
        Integer branchId = 2;
        Integer stock = 20;

        ProductModel productModel = ProductModel.builder().productId(1).branchId(branchId).name(productName).stock(stock).build();

        when(productModelRepository.getProductById(productId)).thenReturn(Mono.just(productModel));
        when(productModelRepository.saveProduct(productModel)).thenReturn(Mono.just(productModel));

        Mono<ProductModel> result = productUseCase.modifyStockInProduct(productId,stock);

        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(productModelResponse ->
                        productModelResponse.getProductId().equals(productId) &&
                        productModelResponse.getName().equals(productName) &&
                        productModelResponse.getBranchId().equals(branchId) &&
                        productModelResponse.getStock().equals(stock)
                )
                .expectNextCount(0).expectComplete().verify();
    }
}