package edu.example.restz.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity                     //1.엔티티 클래스로 만들기
@Table(name="tbl_product")  //2.테이블 이름은 tbl_product로 지정
@Getter                     //3.getter 추가
@ToString(exclude="images") //4.ToString() 추가
@NoArgsConstructor         //5.기본생성자 추가
@AllArgsConstructor         //6.모든 필드를 매개변수로 받는 생성자
@Builder                    //7.빌더 패턴 어노테이션 명시
@EntityListeners(AuditingEntityListener.class)  //8.regDate와 modDate의 등록, 수정 일시 자동으로 추가되도록 처리
public class Product {
    @Id                      //9.pno를 기본 키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //- 1씩 자동 증가
    private Long pno;               //상품 번호

    private String pname;           //상품 이름
    private int price;              //상품 가격
    private String description;     //상품 설명
    private String registerId;      //상품 등록자

    @CreatedDate
    private LocalDateTime regDate;  //상품 등록 일시

    @LastModifiedDate
    private LocalDateTime modDate;  //상품 수정 일시

    @ElementCollection(fetch = FetchType.LAZY) //지연 로딩이 기본값
    @CollectionTable(name="tbl_product_image",
                     joinColumns = @JoinColumn(name="pno"))
    @Builder.Default
    @BatchSize(size=100)
    private SortedSet<ProductImage> images = new TreeSet<>();

    //Product 이미지 추가
    public void addImage(String filename){
        ProductImage productImage = ProductImage.builder().filename(filename)
                                                          .ino(images.size())
                                                          .build();
        images.add(productImage);
    }

    //Product 이미지 제거
    public void clearImages(){
        images.clear();
    }


    //상품 이름, 가격, 설명을 변경하는 메서드 추가
    public void changePname(String pname) {
        this.pname = pname;
    }

    public void changePrice(int price) {
        this.price = price;
    }

    public void changeDescription(String description) {
        this.description = description;
    }
}
