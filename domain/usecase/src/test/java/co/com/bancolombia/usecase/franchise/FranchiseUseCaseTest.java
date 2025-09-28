package co.com.bancolombia.usecase.franchise;

import co.com.bancolombia.model.branchmodel.BranchModel;
import co.com.bancolombia.model.branchmodel.gateways.BranchModelRepository;
import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.model.franchisemodel.FranchiseWithMaxStockProductsModel;
import co.com.bancolombia.model.franchisemodel.gateways.FranchiseModelRepository;
import co.com.bancolombia.model.productmodel.ProductModel;
import co.com.bancolombia.model.productmodel.gateways.ProductModelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FranchiseUseCaseTest {

    @Mock
    private FranchiseModelRepository franchiseModelRepository;
    @Mock
    private BranchModelRepository branchModelRepository;
    @Mock
    private ProductModelRepository productModelRepository;

    @InjectMocks
    private FranchiseUseCase franchiseUseCase;
    @BeforeEach
    void setUp() {
        franchiseModelRepository = mock(FranchiseModelRepository.class);
        branchModelRepository = mock(BranchModelRepository.class);
        productModelRepository = mock(ProductModelRepository.class);
        franchiseUseCase = new FranchiseUseCase(franchiseModelRepository,branchModelRepository,productModelRepository);
    }

    @Test
    void testCreateFranchise() {
        String franchiseName = "Franchise1";
        Integer franchiseId = 1;

        FranchiseModel incomingFranchiseModel = FranchiseModel.builder().name(franchiseName).build();
        FranchiseModel outcommingFranchiseModel = FranchiseModel.builder().franchiseId(franchiseId).name(franchiseName).build();

        when(franchiseModelRepository.createFranchise(incomingFranchiseModel)).thenReturn(Mono.just(outcommingFranchiseModel));

        Mono<FranchiseModel> result = franchiseUseCase.createFranchise(franchiseName);

        StepVerifier.create(result)
                .expectNextMatches(franchiseModel ->
                        franchiseModel.getName().equals(franchiseName)
                )
                .expectNextCount(0);
    }

    @Test
    void testFindMaxStockProductsPerEachBranchByFranchiseId() {
        String franchiseName = "franchise";
        Integer franchiseId = 1;

        ProductModel productMaxInBranch1 = ProductModel.builder().productId(1).branchId(1).name("product1").stock(200).build();
        ProductModel productMaxInBranch2 = ProductModel.builder().productId(2).branchId(2).name("product2").stock(50).build();

        FranchiseModel franchiseReturned = FranchiseModel.builder()
                .franchiseId(franchiseId)
                .name(franchiseName)
                .build();

        BranchModel branchReturned1 = BranchModel.builder()
                .branchId(1)
                .name("branch1")
                .build();

        BranchModel branchReturned2 = BranchModel.builder()
                .branchId(2)
                .name("branch2")
                .build();

        when(franchiseModelRepository.findFranchiseById(franchiseId)).thenReturn(Mono.just(franchiseReturned));
        when(branchModelRepository.findBranchesByFranchiseId(franchiseId)).thenReturn(Flux.just(branchReturned1,branchReturned2));
        when(productModelRepository.findMaxStockProductByBranchId(1)).thenReturn(Mono.just(productMaxInBranch1));
        when(productModelRepository.findMaxStockProductByBranchId(2)).thenReturn(Mono.just(productMaxInBranch2));

        Mono<FranchiseWithMaxStockProductsModel> result = franchiseUseCase.findMaxStockProductsPerEachBranchByFranchiseId(franchiseId);

        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(franchiseModel ->
                        franchiseModel.getName().equals(franchiseName) &&
                        franchiseModel.getFranchiseId().equals(franchiseId) &&
                        franchiseModel.getBranches().get(0).getMaxStockProduct().equals(productMaxInBranch1) &&
                        franchiseModel.getBranches().get(0).getBranchId().equals(1) &&
                        franchiseModel.getBranches().get(0).getName().equals("branch1") &&
                        franchiseModel.getBranches().get(1).getMaxStockProduct().equals(productMaxInBranch2) &&
                        franchiseModel.getBranches().get(1).getBranchId().equals(2) &&
                        franchiseModel.getBranches().get(1).getName().equals("branch2")
                )
                .expectNextCount(0)
                .expectComplete().verify();
    }

}