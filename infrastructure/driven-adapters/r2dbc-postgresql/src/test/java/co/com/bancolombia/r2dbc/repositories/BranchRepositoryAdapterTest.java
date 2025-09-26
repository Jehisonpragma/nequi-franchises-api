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
}
