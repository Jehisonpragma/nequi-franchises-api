package co.com.bancolombia.usecase.franchise;

import co.com.bancolombia.model.branchmodel.BranchModel;
import co.com.bancolombia.model.branchmodel.gateways.BranchModelRepository;
import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.model.reportmaxstocksmodel.ReportMaxStocksModel;
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

        when(franchiseModelRepository.saveFranchise(incomingFranchiseModel)).thenReturn(Mono.just(outcommingFranchiseModel));

        Mono<FranchiseModel> result = franchiseUseCase.createFranchise(franchiseName);

        StepVerifier.create(result)
                .expectNextMatches(franchiseModel ->
                        franchiseModel.getName().equals(franchiseName)
                )
                .expectNextCount(0);
    }

    @Test
    void testUpdateFranchise() {
        String franchiseName = "Franchise1";
        Integer franchiseId = 1;

        FranchiseModel incomingFranchiseModel = FranchiseModel.builder().franchiseId(franchiseId).name(franchiseName).build();
        FranchiseModel outcommingFranchiseModel = FranchiseModel.builder().franchiseId(franchiseId).name(franchiseName).build();

        when(franchiseModelRepository.findFranchiseById(franchiseId)).thenReturn(Mono.just(incomingFranchiseModel));
        when(franchiseModelRepository.saveFranchise(incomingFranchiseModel)).thenReturn(Mono.just(outcommingFranchiseModel));

        Mono<FranchiseModel> result = franchiseUseCase.updateFranchiseName(franchiseId,franchiseName);

        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(franchiseModel ->
                        franchiseModel.getFranchiseId().equals(franchiseId) &&
                        franchiseModel.getName().equals(franchiseName)
                )
                .expectNextCount(0)
                .expectComplete().verify();
    }

}