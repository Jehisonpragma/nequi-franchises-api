package co.com.bancolombia.r2dbc.repositories;

import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.r2dbc.entities.FranchiseEntity;
import co.com.bancolombia.r2dbc.repositories.franchise.FranchiseRepository;
import co.com.bancolombia.r2dbc.repositories.franchise.FranchiseRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
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
    void testCreateFranchise() {
        FranchiseEntity incomingFranchiseEntity = FranchiseEntity.builder().name("Franqui").build();
        FranchiseEntity outcomingFranchiseEntity = FranchiseEntity.builder().franchiseId(1).name("Franqui").build();
        FranchiseModel incomingFranchiseModel = FranchiseModel.builder().name("Franqui").build();
        FranchiseModel outcommingFranchiseModel = FranchiseModel.builder().franchiseId(1).name("Franqui").build();

        when(mapper.map(incomingFranchiseModel, FranchiseEntity.class)).thenReturn(incomingFranchiseEntity);
        when(repository.save(incomingFranchiseEntity)).thenReturn(Mono.just(outcomingFranchiseEntity));
        when(mapper.map(outcomingFranchiseEntity, FranchiseModel.class)).thenReturn(outcommingFranchiseModel);

        Mono<FranchiseModel> result = repositoryAdapter.createFranchise(incomingFranchiseModel);

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
}
