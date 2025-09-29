package co.com.bancolombia.model.reportmaxstocksmodel;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReportMaxStocksModel {

    private Integer franchiseId;
    private String name;
    private List<BranchWithMaxStockProductModel> branches;
}
