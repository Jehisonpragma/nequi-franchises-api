package co.com.bancolombia.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportMaxStocksEntity {

    @Id
    @Column("product_id")
    private Integer productId;
    @Column("product_name")
    private String productName;
    @Column("stock")
    private Integer stock;
    @Column("branch_id")
    private Integer branchId;
    @Column("branch_name")
    private String branchName;
}
