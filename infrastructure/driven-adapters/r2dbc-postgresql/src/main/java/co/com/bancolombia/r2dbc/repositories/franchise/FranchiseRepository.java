package co.com.bancolombia.r2dbc.repositories.franchise;

import co.com.bancolombia.r2dbc.entities.FranchiseEntity;
import co.com.bancolombia.r2dbc.entities.ReportMaxStocksEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface FranchiseRepository extends ReactiveCrudRepository<FranchiseEntity, Integer>, ReactiveQueryByExampleExecutor<FranchiseEntity> {

    @Query("""
            with
            max_products as (
            SELECT p.product_id, p.branch_id, p.name, p.stock
            FROM products p
            JOIN (
                SELECT branch_id, MAX(stock) AS max_stock
                FROM products
                GROUP BY branch_id
            ) m ON p.branch_id = m.branch_id AND p.stock = m.max_stock)
            select mp.product_id as product_id, mp.branch_id as branch_id, mp.name as product_name, mp.stock as stock, b.name as branch_name
            from max_products mp
            join branches b ON b.branch_id = mp.branch_id
            join franchises f ON f.franchise_id = b.franchise_id
            where f.franchise_id = $1
            """)
    Flux<ReportMaxStocksEntity> findReportMaxStockProductByFranchiseId(Integer franchiseId);
}
