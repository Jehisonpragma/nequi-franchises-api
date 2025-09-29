package co.com.bancolombia.model.productmodel.gateways;

import co.com.bancolombia.model.productmodel.ProductModel;
import reactor.core.publisher.Mono;

public interface ProductModelRepository {
    Mono<ProductModel> saveProduct(ProductModel productModel);
    Mono<ProductModel> findProductById(Integer productId);
    Mono<Boolean> deleteProductById(Integer productId);
}
