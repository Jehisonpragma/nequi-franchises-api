package co.com.bancolombia.r2dbc.repositories.branch;

import co.com.bancolombia.model.branchmodel.BranchModel;
import co.com.bancolombia.model.branchmodel.gateways.BranchModelRepository;
import co.com.bancolombia.r2dbc.entities.BranchEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class BranchRepositoryAdapter extends ReactiveAdapterOperations<
        BranchModel,
        BranchEntity,
        Integer,
        BranchRepository> implements BranchModelRepository {

    public BranchRepositoryAdapter(BranchRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, BranchModel.class));
    }

    @Override
    public Mono<BranchModel> createBranch(BranchModel branchModel) {

        return Mono.just(branchModel)
                .map(this::toData)
                .flatMap(entity ->
                        this.repository.save(entity)
                                .map(this::toEntity)
                );

    }
}
