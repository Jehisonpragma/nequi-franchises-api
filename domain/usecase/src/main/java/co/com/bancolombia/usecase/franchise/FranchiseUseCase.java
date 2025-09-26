package co.com.bancolombia.usecase.franchise;

import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.model.franchisemodel.gateways.FranchiseModelRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase {

    private final FranchiseModelRepository franchiseModelRepository;

    public Mono<FranchiseModel> createFranchise(String name){
        FranchiseModel franchiseModel = FranchiseModel.builder().name(name).build();
        return franchiseModelRepository.createFranchise(franchiseModel);
    }
}
