package co.com.bancolombia.r2dbc.repositories.franchise;

import co.com.bancolombia.r2dbc.entities.FranchiseEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FranchiseRepository extends ReactiveCrudRepository<FranchiseEntity, Integer>, ReactiveQueryByExampleExecutor<FranchiseEntity> {

}
