package co.com.bancolombia.usecase.reportmaxstocks;

import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.model.franchisemodel.gateways.FranchiseModelRepository;
import co.com.bancolombia.model.productmodel.ProductModel;
import co.com.bancolombia.model.reportmaxstocksmodel.BranchWithMaxStockProductModel;
import co.com.bancolombia.model.reportmaxstocksmodel.ReportMaxStocksModel;
import co.com.bancolombia.model.reportmaxstocksmodel.gateways.ReportMaxStocksModelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReportMaxStocksUseCaseTest {

    @Mock
    private FranchiseModelRepository franchiseModelRepository;
    @Mock
    private ReportMaxStocksModelRepository reportMaxStocksModelRepository;

    @InjectMocks
    private ReportMaxStocksUseCase reportMaxStocksUseCase;
    @BeforeEach
    void setUp() {
        franchiseModelRepository = mock(FranchiseModelRepository.class);
        reportMaxStocksModelRepository = mock(ReportMaxStocksModelRepository.class);
        reportMaxStocksUseCase = new ReportMaxStocksUseCase(franchiseModelRepository,reportMaxStocksModelRepository);
    }
    @Test
    void findMaxStockProductsPerEachBranchByFranchiseId() {
        String franchiseName = "franchise";
        Integer franchiseId = 1;
        Integer branchId1 = 1;
        Integer branchId2 = 1;

        FranchiseModel franchiseReturned = FranchiseModel.builder()
                .franchiseId(franchiseId)
                .name(franchiseName)
                .build();

        ProductModel productModel1 = ProductModel.builder().productId(1).name("product1").branchId(1).stock(20).build();
        ProductModel productModel2 = ProductModel.builder().productId(2).name("product1").branchId(2).stock(20).build();

        BranchWithMaxStockProductModel branchWithMaxStockProductModel1 = BranchWithMaxStockProductModel.builder()
                .branchId(branchId1)
                .name("branch1")
                .maxStockProduct(productModel1)
                .build();

        BranchWithMaxStockProductModel branchWithMaxStockProductModel2 = BranchWithMaxStockProductModel.builder()
                .branchId(branchId2)
                .name("branch2")
                .maxStockProduct(productModel2)
                .build();

        when(franchiseModelRepository.findFranchiseById(franchiseId)).thenReturn(Mono.just(franchiseReturned));
        when(reportMaxStocksModelRepository.findReportMaxStockProductByFranchiseId(franchiseId)).thenReturn(Flux.just(branchWithMaxStockProductModel1,branchWithMaxStockProductModel2));

        Mono<ReportMaxStocksModel> result = reportMaxStocksUseCase.findMaxStockProductsPerEachBranchByFranchiseId(franchiseId);

        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(reportMaxStocksModel -> {
                    Assertions.assertEquals(franchiseName, reportMaxStocksModel.getName());
                    Assertions.assertEquals(franchiseId, reportMaxStocksModel.getFranchiseId());
                    Assertions.assertEquals(productModel1, reportMaxStocksModel.getBranches().get(0).getMaxStockProduct());
                    Assertions.assertEquals("branch1", reportMaxStocksModel.getBranches().get(0).getName());
                    Assertions.assertEquals(branchId1, reportMaxStocksModel.getBranches().get(0).getBranchId());
                    Assertions.assertEquals(productModel2, reportMaxStocksModel.getBranches().get(1).getMaxStockProduct());
                    Assertions.assertEquals("branch2", reportMaxStocksModel.getBranches().get(1).getName());
                    Assertions.assertEquals(branchId2, reportMaxStocksModel.getBranches().get(1).getBranchId());
                    return true;
                    }
                )
                .expectNextCount(0)
                .expectComplete().verify();
    }
}