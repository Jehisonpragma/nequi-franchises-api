package co.com.bancolombia.usecase.franchise;

import co.com.bancolombia.model.branchmodel.BranchWithMaxStockProductModel;
import co.com.bancolombia.model.branchmodel.gateways.BranchModelRepository;
import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.model.franchisemodel.FranchiseWithMaxStockProductsModel;
import co.com.bancolombia.model.franchisemodel.gateways.FranchiseModelRepository;
import co.com.bancolombia.model.productmodel.gateways.ProductModelRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase {

    private final FranchiseModelRepository franchiseModelRepository;
    private final BranchModelRepository branchModelRepository;
    private final ProductModelRepository productModelRepository;

    public Mono<FranchiseModel> createFranchise(String name){
        FranchiseModel franchiseModel = FranchiseModel.builder().name(name).build();
        return franchiseModelRepository.createFranchise(franchiseModel);
    }

    public Mono<FranchiseWithMaxStockProductsModel> findMaxStockProductsPerEachBranchByFranchiseId(Integer franchiseId){

        return franchiseModelRepository.findFranchiseById(franchiseId)
                .flatMap(franchiseModel ->  branchModelRepository.findBranchesByFranchiseId(franchiseId)
                        .flatMap(branchModel -> productModelRepository.findMaxStockProductByBranchId(branchModel.getBranchId())
                                .flatMap(productModel -> Mono.just(BranchWithMaxStockProductModel.builder()
                                        .branchId(branchModel.getBranchId())
                                        .name(branchModel.getName())
                                        .maxStockProduct(productModel)
                                        .build())))
                        .collectList()
                        .flatMap(branchMaxStockProductModelList -> Mono.just(FranchiseWithMaxStockProductsModel.builder()
                                .franchiseId(franchiseId)
                                .name(franchiseModel.getName())
                                .branches(branchMaxStockProductModelList)
                                .build()))
                );
    }
}
