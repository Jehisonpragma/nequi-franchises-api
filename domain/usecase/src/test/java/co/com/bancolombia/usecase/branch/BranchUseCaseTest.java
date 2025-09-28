package co.com.bancolombia.usecase.branch;

import co.com.bancolombia.model.branchmodel.BranchModel;
import co.com.bancolombia.model.branchmodel.gateways.BranchModelRepository;
import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.model.franchisemodel.gateways.FranchiseModelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BranchUseCaseTest {

    @Mock
    private BranchModelRepository branchModelRepository;
    @Mock
    private FranchiseModelRepository franchiseModelRepository;
    @InjectMocks
    private BranchUseCase branchUseCase;
    @BeforeEach
    void setUp() {
        franchiseModelRepository = mock(FranchiseModelRepository.class);
        branchModelRepository = mock(BranchModelRepository.class);
        branchUseCase = new BranchUseCase(branchModelRepository,franchiseModelRepository);
    }

    @Test
    void testCreateBranch() {
        String branchName = "Branch1";
        Integer franchiseId = 1;

        FranchiseModel franchiseModel = FranchiseModel.builder()
                .franchiseId(franchiseId)
                .name("franchise")
                .build();

        BranchModel incomingBranchModel = BranchModel.builder().name(branchName).build();
        BranchModel outcommingBranchModel = BranchModel.builder().branchId(1).franchiseId(1).name(branchName).build();

        when(franchiseModelRepository.findFranchiseById(franchiseId)).thenReturn(Mono.just(franchiseModel));
        when(branchModelRepository.createBranch(incomingBranchModel)).thenReturn(Mono.just(outcommingBranchModel));

        Mono<BranchModel> result = branchUseCase.createBranch(branchName,franchiseId);

        StepVerifier.create(result)
                .expectNextMatches(branchModel ->
                        branchModel.getName().equals(branchName) &&
                        branchModel.getFranchiseId().equals(franchiseId)
                )
                .expectNextCount(0);
    }

}