package co.com.bancolombia.r2dbc.repositories;

import co.com.bancolombia.model.branchmodel.BranchModel;
import co.com.bancolombia.r2dbc.entities.BranchEntity;
import co.com.bancolombia.r2dbc.repositories.branch.BranchRepository;
import co.com.bancolombia.r2dbc.repositories.branch.BranchRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchRepositoryAdapterTest {

    @InjectMocks
    BranchRepositoryAdapter repositoryAdapter;

    @Mock
    BranchRepository repository;

    @Mock
    ObjectMapper mapper;

    @Test
    void testCreateBranch() {
        BranchEntity incomingBranchEntity = BranchEntity.builder().name("Franqui").build();
        BranchEntity outcomingBranchEntity = BranchEntity.builder().branchId(1).name("Franqui").build();
        BranchModel incomingBranchModel = BranchModel.builder().name("Franqui").build();
        BranchModel outcommingBranchModel = BranchModel.builder().branchId(1).name("Franqui").build();

        when(mapper.map(incomingBranchModel, BranchEntity.class)).thenReturn(incomingBranchEntity);
        when(repository.save(incomingBranchEntity)).thenReturn(Mono.just(outcomingBranchEntity));
        when(mapper.map(outcomingBranchEntity, BranchModel.class)).thenReturn(outcommingBranchModel);

        Mono<BranchModel> result = repositoryAdapter.createBranch(incomingBranchModel);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(outcommingBranchModel))
                .verifyComplete();
    }

    @Test
    void testFindBranchesByFranchiseId() {
        Integer franchiseId = 1;
        BranchEntity branchEntity1 = BranchEntity.builder().branchId(1).name("Franqui1").build();
        BranchEntity branchEntity2 = BranchEntity.builder().branchId(2).name("Franqui2").build();
        BranchModel branchModel1 = BranchModel.builder().branchId(1).name("Franqui1").build();
        BranchModel branchModel2 = BranchModel.builder().branchId(2).name("Franqui2").build();

        when(repository.findBranchesByFranchiseId(franchiseId)).thenReturn(Flux.just(branchEntity1,branchEntity2));
        when(mapper.map(branchEntity1, BranchModel.class)).thenReturn(branchModel1);
        when(mapper.map(branchEntity2, BranchModel.class)).thenReturn(branchModel2);

        Flux<BranchModel> result = repositoryAdapter.findBranchesByFranchiseId(franchiseId);

        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(value -> value.equals(branchModel1))
                .expectNextMatches(value -> value.equals(branchModel2))
                .expectComplete().verify();
    }
}
