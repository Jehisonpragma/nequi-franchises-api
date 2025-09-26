package co.com.bancolombia.r2dbc.repositories;

import co.com.bancolombia.model.productmodel.ProductModel;
import co.com.bancolombia.r2dbc.entities.ProductEntity;
import co.com.bancolombia.r2dbc.repositories.product.ProductRepository;
import co.com.bancolombia.r2dbc.repositories.product.ProductRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryAdapterTest {

    @InjectMocks
    ProductRepositoryAdapter repositoryAdapter;

    @Mock
    ProductRepository repository;

    @Mock
    ObjectMapper mapper;

    @Test
    void testCreateProduct() {
        ProductEntity incomingProductEntity = ProductEntity.builder().name("product1").branchId(1).stock(20).build();
        ProductEntity outcomingProductEntity = ProductEntity.builder().productId(1).name("product1").branchId(1).stock(20).build();
        ProductModel incomingProductModel = ProductModel.builder().name("product1").branchId(1).stock(20).build();
        ProductModel outcommingProductModel = ProductModel.builder().productId(1).name("product1").branchId(1).stock(20).build();

        when(mapper.map(incomingProductModel, ProductEntity.class)).thenReturn(incomingProductEntity);
        when(repository.save(incomingProductEntity)).thenReturn(Mono.just(outcomingProductEntity));
        when(mapper.map(outcomingProductEntity, ProductModel.class)).thenReturn(outcommingProductModel);

        Mono<ProductModel> result = repositoryAdapter.createProduct(incomingProductModel);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(outcommingProductModel))
                .verifyComplete();
    }
}
