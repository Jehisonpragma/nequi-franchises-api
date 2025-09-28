package co.com.bancolombia.usecase.branch;

import co.com.bancolombia.model.branchmodel.BranchModel;
import co.com.bancolombia.model.branchmodel.gateways.BranchModelRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BranchUseCase {

    private final BranchModelRepository branchModelRepository;

    public Mono<BranchModel> createBranch(String name, Integer franchiseId){
        BranchModel branchModel = BranchModel.builder().franchiseId(franchiseId).name(name).build();
        return branchModelRepository.createBranch(branchModel);
    }
}
