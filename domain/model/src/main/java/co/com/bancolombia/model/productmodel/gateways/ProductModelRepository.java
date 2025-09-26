package co.com.bancolombia.model.productmodel.gateways;

import co.com.bancolombia.model.productmodel.ProductModel;
import reactor.core.publisher.Mono;

public interface ProductModelRepository {
    Mono<ProductModel> createProduct(ProductModel productModel);
    Mono<Boolean> deleteProductById(Integer productId);
}
