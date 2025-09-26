package co.com.bancolombia.usecase.franchise;

import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.model.franchisemodel.gateways.FranchiseModelRepository;
import co.com.bancolombia.usecase.franchise.FranchiseUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FranchiseUseCaseTest {

    @Mock
    private FranchiseModelRepository franchiseModelRepository;
    @InjectMocks
    private FranchiseUseCase franchiseUseCase;
    @BeforeEach
    void setUp() {
        franchiseModelRepository = mock(FranchiseModelRepository.class);
        franchiseUseCase = new FranchiseUseCase(franchiseModelRepository);
    }

    @Test
    void testCreateFranchise() {
        String franchiseName = "Franchise1";
        Integer franchiseId = 1;

        FranchiseModel incomingFranchiseModel = FranchiseModel.builder().name(franchiseName).build();
        FranchiseModel outcommingFranchiseModel = FranchiseModel.builder().franchiseId(1).name(franchiseName).build();

        when(franchiseModelRepository.createFranchise(incomingFranchiseModel)).thenReturn(Mono.just(outcommingFranchiseModel));

        Mono<FranchiseModel> result = franchiseUseCase.createFranchise(franchiseName);

        StepVerifier.create(result)
                .expectNextMatches(franchiseModel ->
                        franchiseModel.getName().equals(franchiseName)
                )
                .expectNextCount(0);
    }

}