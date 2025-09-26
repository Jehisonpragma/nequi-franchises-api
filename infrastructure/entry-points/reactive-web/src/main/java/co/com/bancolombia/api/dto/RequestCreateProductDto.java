package co.com.bancolombia.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateProductDto {
    private String name;
    private Integer branchId;
    private Integer stock;
}
