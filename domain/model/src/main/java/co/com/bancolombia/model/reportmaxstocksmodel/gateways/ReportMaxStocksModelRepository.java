package co.com.bancolombia.model.reportmaxstocksmodel.gateways;

import co.com.bancolombia.model.reportmaxstocksmodel.BranchWithMaxStockProductModel;
import reactor.core.publisher.Flux;

public interface ReportMaxStocksModelRepository {
    Flux<BranchWithMaxStockProductModel> findReportMaxStockProductByFranchiseId(Integer franchiseId);
}
