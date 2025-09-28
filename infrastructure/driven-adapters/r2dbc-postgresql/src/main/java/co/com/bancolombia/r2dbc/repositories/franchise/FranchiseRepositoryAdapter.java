package co.com.bancolombia.r2dbc.repositories.franchise;

import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.model.franchisemodel.gateways.FranchiseModelRepository;
import co.com.bancolombia.r2dbc.entities.FranchiseEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class FranchiseRepositoryAdapter extends ReactiveAdapterOperations<
        FranchiseModel,
        FranchiseEntity,
        Integer,
        FranchiseRepository> implements FranchiseModelRepository {

    public FranchiseRepositoryAdapter(FranchiseRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, FranchiseModel.class));
    }

    @Override
    public Mono<FranchiseModel> createFranchise(FranchiseModel franchiseModel) {

        return Mono.just(franchiseModel)
                .map(this::toData)
                .flatMap(entity ->
                        this.repository.save(entity)
                                .map(this::toEntity)
                );

    }

    @Override
    public Mono<FranchiseModel> findFranchiseById(Integer franchiseId) {
        return this.repository.findById(franchiseId).map(this::toEntity);
    }
}
