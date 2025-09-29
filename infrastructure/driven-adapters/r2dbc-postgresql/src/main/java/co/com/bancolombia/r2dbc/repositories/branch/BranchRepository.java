package co.com.bancolombia.r2dbc.repositories.branch;

import co.com.bancolombia.r2dbc.entities.BranchEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BranchRepository extends ReactiveCrudRepository<BranchEntity, Integer>, ReactiveQueryByExampleExecutor<BranchEntity> {

    @Query("SELECT * FROM branches WHERE franchise_id = $1")
    Flux<BranchEntity> findBranchesByFranchiseId(Integer franchiseId);
}
