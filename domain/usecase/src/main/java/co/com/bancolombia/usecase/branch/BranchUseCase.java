package co.com.bancolombia.usecase.branch;

import co.com.bancolombia.model.branchmodel.BranchModel;
import co.com.bancolombia.model.branchmodel.gateways.BranchModelRepository;
import co.com.bancolombia.model.exceptionmodel.BusinessException;
import co.com.bancolombia.model.exceptionmodel.ErrorCode;
import co.com.bancolombia.model.franchisemodel.gateways.FranchiseModelRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BranchUseCase {

    private final BranchModelRepository branchModelRepository;
    private final FranchiseModelRepository franchiseModelRepository;

    public Mono<BranchModel> createBranch(String name, Integer franchiseId){
        BranchModel branchModel = BranchModel.builder().franchiseId(franchiseId).name(name).build();

        return franchiseModelRepository.findFranchiseById(franchiseId).hasElement()
                .flatMap(hasFranchise -> Boolean.TRUE.equals(hasFranchise)
                    ? branchModelRepository.saveBranch(branchModel)
                    : Mono.error(new BusinessException(ErrorCode.E422000)));
    }

    public Mono<BranchModel> updateBranchName(Integer branchId, String name){

        return branchModelRepository.findBranchById(branchId)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.E422001)))
                .flatMap(branchModel ->{
                    branchModel.setName(name);
                    return branchModelRepository.saveBranch(branchModel);
                });
    }
}
