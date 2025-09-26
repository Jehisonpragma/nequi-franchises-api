package co.com.bancolombia.r2dbc.repositories.product;

import co.com.bancolombia.model.productmodel.ProductModel;
import co.com.bancolombia.model.productmodel.gateways.ProductModelRepository;
import co.com.bancolombia.r2dbc.entities.ProductEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ProductRepositoryAdapter extends ReactiveAdapterOperations<
        ProductModel,
        ProductEntity,
        Integer,
        ProductRepository> implements ProductModelRepository {

    public ProductRepositoryAdapter(ProductRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, ProductModel.class));
    }

    @Override
    public Mono<ProductModel> saveProduct(ProductModel productModel) {

        return Mono.just(productModel)
                .map(this::toData)
                .flatMap(entity ->
                        this.repository.save(entity)
                                .map(this::toEntity)
                );

    }

    @Override
    public Mono<ProductModel> getProductById(Integer productId) {
        return Mono.just(productId)
                .flatMap(productIdProcessed ->
                        this.repository.findById(productIdProcessed)
                                .map(this::toEntity)
                );
    }

    @Override
    public Mono<Boolean> deleteProductById(Integer productId) {
        return this.repository.deleteById(productId)
                .thenReturn(Boolean.TRUE);
    }

}
