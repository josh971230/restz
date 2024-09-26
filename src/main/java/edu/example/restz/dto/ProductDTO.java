package edu.example.restz.dto;

import edu.example.restz.entity.Product;
import edu.example.restz.entity.ProductImage;
import edu.example.restz.entity.Todo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ProductDTO {
    private Long reviewCount;
    private Long pno; //1. 필드 6개 선언

    @NotEmpty
    private String pname;

    @Min(0)
    private int price;

    private String description;

    @NotEmpty
    private String registerId;

    private List<String> images; //이미지 파일이름 목록

    public ProductDTO(Product product) { //2. Product를 매개변수로 받아서
        this.pno = product.getPno();     //   현재 객체의 필드들을 초기화하는 생성자
        this.pname = product.getPname();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.registerId = product.getRegisterId();
        this.images = product.getImages()
                             .stream()
                             .map(ProductImage::getFilename)
                             .collect(Collectors.toList());
    }

    public Product toEntity(){     //3. Product 객체의 builder()를 이용하여  현재 객체의 값을 Product에 저장한 후
       Product product = Product.builder().pno(pno)    // 반환하는 toEntity() 메서드
                                          .pname(pname)
                                          .price(price)
                                          .description(description)
                                          .registerId(registerId)
                                          .build();

        if( images != null || !images.isEmpty() ) { //   단, 상품이미지들이 있는 경우에는
            images.forEach(product::addImage);      //   Product에 추가하여 반환하도록 처리
        }
        return product;
    }

}









