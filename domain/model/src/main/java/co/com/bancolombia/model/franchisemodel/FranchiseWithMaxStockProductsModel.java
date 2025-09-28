package co.com.bancolombia.model.franchisemodel;
import co.com.bancolombia.model.branchmodel.BranchWithMaxStockProductModel;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FranchiseWithMaxStockProductsModel {

    private Integer franchiseId;
    private String name;
    private List<BranchWithMaxStockProductModel> branches;
}
