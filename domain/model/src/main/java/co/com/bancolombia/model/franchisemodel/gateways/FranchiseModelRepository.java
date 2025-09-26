package co.com.bancolombia.model.franchisemodel.gateways;

import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import reactor.core.publisher.Mono;

public interface FranchiseModelRepository {

    Mono<FranchiseModel> createFranchise(FranchiseModel franchiseModel);
}
