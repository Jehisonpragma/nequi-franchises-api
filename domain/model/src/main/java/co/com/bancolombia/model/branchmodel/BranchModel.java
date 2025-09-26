package co.com.bancolombia.model.branchmodel;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BranchModel {

    private Integer branchId;
    private Integer franchiseId;
    private String name;
}
