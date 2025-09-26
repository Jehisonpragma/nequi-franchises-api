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
@Table("products")
public class ProductEntity {

    @Id
    @Column("product_id")
    private Integer productId;
    @Column("branch_id")
    private Integer branchId;
    @Column("name")
    private String name;
    @Column("stock")
    private Integer stock;
}
