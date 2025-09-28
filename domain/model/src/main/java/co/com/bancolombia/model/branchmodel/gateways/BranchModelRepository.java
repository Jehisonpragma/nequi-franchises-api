package co.com.bancolombia.model.branchmodel.gateways;

import co.com.bancolombia.model.branchmodel.BranchModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchModelRepository {
    Mono<BranchModel> createBranch(BranchModel branchModel);
    Flux<BranchModel> findBranchesByFranchiseId(Integer franchiseId);
}
