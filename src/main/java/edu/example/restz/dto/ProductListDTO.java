package edu.example.restz.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductListDTO {
    private Long reviewCount;
    private Long pno;
    private String pname;
    private int price;
    private String registerId;
    private String pimage;
}
