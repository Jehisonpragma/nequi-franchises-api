package co.com.bancolombia.usecase.franchise;

import co.com.bancolombia.model.branchmodel.BranchWithMaxStockProductModel;
import co.com.bancolombia.model.branchmodel.gateways.BranchModelRepository;
import co.com.bancolombia.model.exceptionmodel.BusinessException;
import co.com.bancolombia.model.exceptionmodel.ErrorCode;
import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.model.franchisemodel.FranchiseWithMaxStockProductsModel;
import co.com.bancolombia.model.franchisemodel.gateways.FranchiseModelRepository;
import co.com.bancolombia.model.productmodel.gateways.ProductModelRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RequiredArgsConstructor
public class FranchiseUseCase {

    private final FranchiseModelRepository franchiseModelRepository;
    private final BranchModelRepository branchModelRepository;
    private final ProductModelRepository productModelRepository;

    public Mono<FranchiseModel> createFranchise(String name){
        if(Objects.isNull(name) || name.isBlank()){
            return Mono.error(new BusinessException(ErrorCode.E422003));
        }
        FranchiseModel franchiseModel = FranchiseModel.builder().name(name).build();
        return franchiseModelRepository.saveFranchise(franchiseModel);
    }

    public Mono<FranchiseModel> updateFranchiseName(Integer franchiseId, String name){

        return franchiseModelRepository.findFranchiseById(franchiseId)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.E422000)))
                .flatMap(franchiseModel ->{
                        franchiseModel.setName(name);
                        return franchiseModelRepository.saveFranchise(franchiseModel);
                        });
    }

    public Mono<FranchiseWithMaxStockProductsModel> findMaxStockProductsPerEachBranchByFranchiseId(Integer franchiseId){

        return franchiseModelRepository.findFranchiseById(franchiseId)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.E422000)))
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
