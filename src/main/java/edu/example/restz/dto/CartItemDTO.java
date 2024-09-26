package edu.example.restz.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemDTO {
    private String customer;
    private Long pno;
    private int quantity;
    private Long itemNo;
    private String pname;
    private int price;
    private String image;
}









