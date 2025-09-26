package co.com.bancolombia.usecase.product;

import co.com.bancolombia.model.productmodel.ProductModel;
import co.com.bancolombia.model.productmodel.gateways.ProductModelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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

        ProductModel incomingProductModel = ProductModel.builder().name(productName).build();
        ProductModel outcommingProductModel = ProductModel.builder().productId(1).branchId(branchId).name(productName).build();

        when(productModelRepository.createProduct(incomingProductModel)).thenReturn(Mono.just(outcommingProductModel));

        Mono<ProductModel> result = productUseCase.createProduct(productName, branchId,stock);

        StepVerifier.create(result)
                .expectNextMatches(productModel ->
                        productModel.getName().equals(productName) &&
                                productModel.getBranchId().equals(branchId) &&
                                productModel.getStock().equals(stock)
                )
                .expectNextCount(0);
    }
}