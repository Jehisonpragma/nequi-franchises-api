package co.com.bancolombia.r2dbc.repositories;

import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.model.productmodel.ProductModel;
import co.com.bancolombia.model.reportmaxstocksmodel.BranchWithMaxStockProductModel;
import co.com.bancolombia.r2dbc.entities.FranchiseEntity;
import co.com.bancolombia.r2dbc.entities.ProductEntity;
import co.com.bancolombia.r2dbc.entities.ReportMaxStocksEntity;
import co.com.bancolombia.r2dbc.repositories.franchise.FranchiseRepository;
import co.com.bancolombia.r2dbc.repositories.franchise.FranchiseRepositoryAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseRepositoryAdapterTest {

    @InjectMocks
    FranchiseRepositoryAdapter repositoryAdapter;

    @Mock
    FranchiseRepository repository;

    @Mock
    ObjectMapper mapper;

    @Test
    void testSaveFranchise() {
        FranchiseEntity incomingFranchiseEntity = FranchiseEntity.builder().name("Franqui").build();
        FranchiseEntity outcomingFranchiseEntity = FranchiseEntity.builder().franchiseId(1).name("Franqui").build();
        FranchiseModel incomingFranchiseModel = FranchiseModel.builder().name("Franqui").build();
        FranchiseModel outcommingFranchiseModel = FranchiseModel.builder().franchiseId(1).name("Franqui").build();

        when(mapper.map(incomingFranchiseModel, FranchiseEntity.class)).thenReturn(incomingFranchiseEntity);
        when(repository.save(incomingFranchiseEntity)).thenReturn(Mono.just(outcomingFranchiseEntity));
        when(mapper.map(outcomingFranchiseEntity, FranchiseModel.class)).thenReturn(outcommingFranchiseModel);

        Mono<FranchiseModel> result = repositoryAdapter.saveFranchise(incomingFranchiseModel);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(outcommingFranchiseModel))
                .verifyComplete();
    }

    @Test
    void testFindFranchiseById() {
        Integer franchiseId = 1;
        FranchiseEntity franchiseEntity = FranchiseEntity.builder().franchiseId(1).name("Franqui").build();
        FranchiseModel franchiseModel = FranchiseModel.builder().franchiseId(1).name("Franqui").build();

        when(repository.findById(franchiseId)).thenReturn(Mono.just(franchiseEntity));
        when(mapper.map(franchiseEntity, FranchiseModel.class)).thenReturn(franchiseModel);

        Mono<FranchiseModel> result = repositoryAdapter.findFranchiseById(franchiseId);

        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(value -> value.equals(franchiseModel))
                .expectComplete().verify();
    }

    @Test
    void testFindMaxStockProductByFranchiseId() {

        Integer branchId1 = 1;
        Integer branchId2 = 1;
        Integer franchiseId = 1;
        ProductModel productModel1 = ProductModel.builder().productId(1).name("product1").branchId(1).stock(20).build();
        ProductModel productModel2 = ProductModel.builder().productId(2).name("product1").branchId(2).stock(620).build();

        BranchWithMaxStockProductModel branchWithMaxStockProductModel1 = BranchWithMaxStockProductModel.builder()
                .branchId(branchId1)
                .maxStockProduct(productModel1)
                .build();

        BranchWithMaxStockProductModel branchWithMaxStockProductModel2 = BranchWithMaxStockProductModel.builder()
                .branchId(branchId2)
                .maxStockProduct(productModel2)
                .build();

        ReportMaxStocksEntity reportMaxStocksEntity1 = ReportMaxStocksEntity.builder()
                .branchId(branchId1)
                .productId(1)
                .productName("product1")
                .stock(20)
                .build();

        ReportMaxStocksEntity reportMaxStocksEntity2 = ReportMaxStocksEntity.builder()
                .branchId(branchId2)
                .productId(2)
                .productName("product2")
                .stock(620)
                .build();

        when(repository.findReportMaxStockProductByFranchiseId(franchiseId)).thenReturn(Flux.just(reportMaxStocksEntity1,reportMaxStocksEntity2));

        Flux<BranchWithMaxStockProductModel> result = repositoryAdapter.findReportMaxStockProductByFranchiseId(franchiseId);

        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(value -> {
                    Assertions.assertEquals(branchWithMaxStockProductModel1.getMaxStockProduct().getStock(),value.getMaxStockProduct().getStock());
                    return true;
                })
                .expectNextMatches(value -> {
                    Assertions.assertEquals(branchWithMaxStockProductModel2.getMaxStockProduct().getStock(),value.getMaxStockProduct().getStock());
                    return true;
                })
                .expectComplete().verify();
    }
}
