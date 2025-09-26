package co.com.bancolombia.model.franchisemodel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FranchiseModel {

    private Integer franchiseId;
    private String name;
}
