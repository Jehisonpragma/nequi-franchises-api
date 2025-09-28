package co.com.bancolombia.usecase.product;

import co.com.bancolombia.model.branchmodel.gateways.BranchModelRepository;
import co.com.bancolombia.model.exceptionmodel.BusinessException;
import co.com.bancolombia.model.exceptionmodel.ErrorCode;
import co.com.bancolombia.model.productmodel.ProductModel;
import co.com.bancolombia.model.productmodel.gateways.ProductModelRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductModelRepository productModelRepository;
    private final BranchModelRepository branchModelRepository;

    public Mono<ProductModel> createProduct(String name, Integer branchId, Integer stock){
        ProductModel productModel = ProductModel.builder().branchId(branchId).name(name).stock(stock).build();

        return branchModelRepository.findBranchById(branchId).hasElement()
                .flatMap(hasBranch -> Boolean.TRUE.equals(hasBranch)
                ? productModelRepository.saveProduct(productModel)
                : Mono.error(new BusinessException(ErrorCode.E422001)));
    }

    public Mono<ProductModel> updateProductName(Integer productId, String name){

        return productModelRepository.findProductById(productId)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.E422002)))
                .flatMap(productModel ->{
                    productModel.setName(name);
                    return productModelRepository.saveProduct(productModel);
                });
    }

    public Mono<Boolean> deleteProduct(Integer productId) {
        return productModelRepository.findProductById(productId).hasElement()
                .flatMap(hasProduct -> Boolean.TRUE.equals(hasProduct)
                        ? productModelRepository.deleteProductById(productId)
                        : Mono.error(new BusinessException(ErrorCode.E422002)));
    }

    public Mono<ProductModel> modifyStockInProduct(Integer productId, Integer stock){

        return productModelRepository.findProductById(productId)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.E422002)))
                .flatMap(productModel -> {
                    productModel.setStock(stock);
                    return productModelRepository.saveProduct(productModel);
                });
    }
}
