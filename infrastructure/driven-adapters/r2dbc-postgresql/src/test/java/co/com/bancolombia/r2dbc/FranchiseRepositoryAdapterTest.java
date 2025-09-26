package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.r2dbc.entities.FranchiseEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
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

//    @Test
//    void mustFindValueById() {
//
//        when(repository.findById("1")).thenReturn(Mono.just("test"));
//        when(mapper.map("test", Object.class)).thenReturn("test");
//
//        Mono<Object> result = repositoryAdapter.findById("1");
//
//        StepVerifier.create(result)
//                .expectNextMatches(value -> value.equals("test"))
//                .verifyComplete();
//    }
//
//    @Test
//    void mustFindAllValues() {
//        when(repository.findAll()).thenReturn(Flux.just("test"));
//        when(mapper.map("test", Object.class)).thenReturn("test");
//
//        Flux<Object> result = repositoryAdapter.findAll();
//
//        StepVerifier.create(result)
//                .expectNextMatches(value -> value.equals("test"))
//                .verifyComplete();
//    }
//
//    @Test
//    void mustFindByExample() {
//        when(repository.findAll(any(Example.class))).thenReturn(Flux.just("test"));
//        when(mapper.map("test", Object.class)).thenReturn("test");
//
//        Flux<Object> result = repositoryAdapter.findByExample("test");
//
//        StepVerifier.create(result)
//                .expectNextMatches(value -> value.equals("test"))
//                .verifyComplete();
//    }

//    @Test
//    void mustSaveValue() {
//        FranchiseEntity franchiseEntity = FranchiseEntity.builder().franchiseId(1).name("Franqui").build();
//        FranchiseModel franchiseModel = FranchiseModel.builder().name("Franqui").build();
//
//        when(repository.save(franchiseEntity)).thenReturn(Mono.just(franchiseEntity));
//        when(mapper.map(franchiseEntity, FranchiseModel.class)).thenReturn(franchiseModel);
//        when(mapper.map(franchiseModel, FranchiseEntity.class)).thenReturn(franchiseEntity);
//
//        Mono<FranchiseModel> result = repositoryAdapter.save(franchiseModel);
//
//        StepVerifier.create(result)
//                .expectNextMatches(value -> value.equals(franchiseModel))
//                .verifyComplete();
//    }

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
}
