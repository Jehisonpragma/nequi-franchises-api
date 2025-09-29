package co.com.bancolombia.usecase.reportmaxstocks;

import co.com.bancolombia.model.exceptionmodel.BusinessException;
import co.com.bancolombia.model.exceptionmodel.ErrorCode;
import co.com.bancolombia.model.franchisemodel.gateways.FranchiseModelRepository;
import co.com.bancolombia.model.reportmaxstocksmodel.ReportMaxStocksModel;
import co.com.bancolombia.model.reportmaxstocksmodel.gateways.ReportMaxStocksModelRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ReportMaxStocksUseCase {

    private final FranchiseModelRepository franchiseModelRepository;
    private final ReportMaxStocksModelRepository reportMaxStocksModelRepository;

    public Mono<ReportMaxStocksModel> findMaxStockProductsPerEachBranchByFranchiseId(Integer franchiseId){

        return franchiseModelRepository.findFranchiseById(franchiseId)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.E422000)))
                .flatMap(franchiseModel ->  reportMaxStocksModelRepository.findReportMaxStockProductByFranchiseId(franchiseId)
                        .collectList()
                        .flatMap(branchMaxStockProductModelList -> Mono.just(ReportMaxStocksModel.builder()
                                .franchiseId(franchiseId)
                                .name(franchiseModel.getName())
                                .branches(branchMaxStockProductModelList)
                                .build()))
                );
    }

}
