package co.com.bancolombia.usecase.branch;

import co.com.bancolombia.model.branchmodel.BranchModel;
import co.com.bancolombia.model.branchmodel.gateways.BranchModelRepository;
import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.model.franchisemodel.gateways.FranchiseModelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
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

        BranchModel outcommingBranchModel = BranchModel.builder().branchId(1).franchiseId(1).name(branchName).build();

        when(franchiseModelRepository.findFranchiseById(franchiseId)).thenReturn(Mono.just(franchiseModel));
        when(branchModelRepository.saveBranch(any(BranchModel.class))).thenReturn(Mono.just(outcommingBranchModel));

        Mono<BranchModel> result = branchUseCase.createBranch(branchName,franchiseId);

        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(branchModel -> {
                            Assertions.assertEquals(branchName,branchModel.getName());
                            Assertions.assertEquals(franchiseId, branchModel.getFranchiseId());
                            return true;
                        }
                )
                .expectNextCount(0)
                .expectComplete().verify();
    }

    @Test
    void testUpdateBranch() {
        String branchName = "Branch1";
        Integer branchId = 1;

        BranchModel incomingBranchModel = BranchModel.builder().branchId(branchId).name(branchName).build();
        BranchModel outcommingBranchModel = BranchModel.builder().branchId(branchId).name(branchName).build();

        when(branchModelRepository.findBranchById(branchId)).thenReturn(Mono.just(incomingBranchModel));
        when(branchModelRepository.saveBranch(incomingBranchModel)).thenReturn(Mono.just(outcommingBranchModel));

        Mono<BranchModel> result = branchUseCase.updateBranchName(branchId,branchName);

        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(branchModel ->
                        branchModel.getBranchId().equals(branchId) &&
                                branchModel.getName().equals(branchName)
                )
                .expectNextCount(0)
                .expectComplete().verify();
    }

}