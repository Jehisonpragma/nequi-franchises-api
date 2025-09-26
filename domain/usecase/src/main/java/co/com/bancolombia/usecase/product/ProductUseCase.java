package co.com.bancolombia.usecase.product;

import co.com.bancolombia.model.productmodel.ProductModel;
import co.com.bancolombia.model.productmodel.gateways.ProductModelRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductModelRepository productModelRepository;

    public Mono<ProductModel> createProduct(String name, Integer branchId, Integer stock){
        ProductModel productModel = ProductModel.builder().branchId(branchId).name(name).stock(stock).build();
        return productModelRepository.saveProduct(productModel);
    }

    public Mono<Boolean> deleteProduct(Integer productId) {
        return Mono.just(productId)
                .flatMap(productModelRepository::deleteProductById);
    }

    public Mono<ProductModel> modifyStockInProduct(Integer productId, Integer stock){

        return Mono.just(productId).flatMap( productIdReceived ->
                productModelRepository.getProductById(productIdReceived).flatMap(productModel -> {
                    productModel.setStock(stock);
                    return productModelRepository.saveProduct(productModel);
                })
        );
    }
}
