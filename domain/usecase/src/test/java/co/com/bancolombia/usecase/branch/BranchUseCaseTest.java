package co.com.bancolombia.usecase.branch;

import co.com.bancolombia.model.branchmodel.BranchModel;
import co.com.bancolombia.model.branchmodel.gateways.BranchModelRepository;
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
    @InjectMocks
    private BranchUseCase branchUseCase;
    @BeforeEach
    void setUp() {
        branchModelRepository = mock(BranchModelRepository.class);
        branchUseCase = new BranchUseCase(branchModelRepository);
    }

    @Test
    void testCreateBranch() {
        String branchName = "Branch1";
        Integer franchiseId = 1;

        BranchModel incomingBranchModel = BranchModel.builder().name(branchName).build();
        BranchModel outcommingBranchModel = BranchModel.builder().branchId(1).franchiseId(1).name(branchName).build();

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