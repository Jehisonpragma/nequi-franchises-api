package co.com.bancolombia.r2dbc.repositories.product;

import co.com.bancolombia.r2dbc.entities.ProductEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Integer>, ReactiveQueryByExampleExecutor<ProductEntity> {

}
