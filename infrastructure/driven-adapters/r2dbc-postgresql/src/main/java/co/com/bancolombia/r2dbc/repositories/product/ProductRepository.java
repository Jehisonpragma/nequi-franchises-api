package co.com.bancolombia.r2dbc.repositories.product;

import co.com.bancolombia.r2dbc.entities.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Integer>, ReactiveQueryByExampleExecutor<ProductEntity> {

    @Query("SELECT * FROM products WHERE branch_id = $1 order by stock desc limit 1")
    Mono<ProductEntity> findMaxStockProductByBranchId(Integer branchId);
}
