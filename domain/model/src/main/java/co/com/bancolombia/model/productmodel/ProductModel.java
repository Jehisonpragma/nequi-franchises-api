package co.com.bancolombia.model.productmodel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductModel {

    private Integer productId;
    private Integer branchId;
    private String name;
    private Integer stock;
}
