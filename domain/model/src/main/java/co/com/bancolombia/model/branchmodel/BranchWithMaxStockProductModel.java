package co.com.bancolombia.model.branchmodel;
import co.com.bancolombia.model.productmodel.ProductModel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BranchWithMaxStockProductModel {

    private Integer branchId;
    private String name;
    private ProductModel maxStockProduct;
}
