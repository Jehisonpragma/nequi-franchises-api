package co.com.bancolombia.r2dbc.repositories.franchise;

import co.com.bancolombia.model.franchisemodel.FranchiseModel;
import co.com.bancolombia.model.franchisemodel.gateways.FranchiseModelRepository;
import co.com.bancolombia.model.productmodel.ProductModel;
import co.com.bancolombia.model.reportmaxstocksmodel.BranchWithMaxStockProductModel;
import co.com.bancolombia.model.reportmaxstocksmodel.gateways.ReportMaxStocksModelRepository;
import co.com.bancolombia.r2dbc.entities.FranchiseEntity;
import co.com.bancolombia.r2dbc.entities.ReportMaxStocksEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class FranchiseRepositoryAdapter extends ReactiveAdapterOperations<
        FranchiseModel,
        FranchiseEntity,
        Integer,
        FranchiseRepository> implements FranchiseModelRepository, ReportMaxStocksModelRepository {

    public FranchiseRepositoryAdapter(FranchiseRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, FranchiseModel.class));
    }

    @Override
    public Mono<FranchiseModel> saveFranchise(FranchiseModel franchiseModel) {

        return Mono.just(franchiseModel)
                .map(this::toData)
                .flatMap(entity ->
                        this.repository.save(entity)
                                .map(this::toEntity)
                );

    }

    @Override
    public Mono<FranchiseModel> findFranchiseById(Integer franchiseId) {
        return this.repository.findById(franchiseId).map(this::toEntity);
    }

    @Override
    public Flux<BranchWithMaxStockProductModel> findReportMaxStockProductByFranchiseId(Integer franchiseId) {
        return this.repository.findReportMaxStockProductByFranchiseId(franchiseId)
                .map(this::mapFranchiseWithMaxStockProductsModel);
    }

    private BranchWithMaxStockProductModel mapFranchiseWithMaxStockProductsModel(ReportMaxStocksEntity reportMaxStocksEntity){
        return BranchWithMaxStockProductModel.builder()
                .branchId(reportMaxStocksEntity.getBranchId())
                .name(reportMaxStocksEntity.getBranchName())
                .maxStockProduct(ProductModel.builder()
                        .productId(reportMaxStocksEntity.getProductId())
                        .branchId(reportMaxStocksEntity.getBranchId())
                        .name(reportMaxStocksEntity.getProductName())
                        .stock(reportMaxStocksEntity.getStock())
                        .build())
                .build();
    }
}
