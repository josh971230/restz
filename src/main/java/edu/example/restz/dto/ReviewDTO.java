package edu.example.restz.dto;

import edu.example.restz.entity.Product;
import edu.example.restz.entity.ProductImage;
import edu.example.restz.entity.Review;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long rno;
    private String content;
    private String reviewer;
    private int star;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private Long pno;

    public ReviewDTO(Review review) {
        this.rno = review.getRno();
        this.content = review.getContent();
        this.reviewer = review.getReviewer();
        this.star = review.getStar();
        this.regDate = review.getRegDate();
        this.modDate = review.getModDate();
        this.pno = review.getProduct().getPno();
    }

    public Review toEntity(){
        Product product = Product.builder().pno(pno).build();
        return Review.builder().rno(rno)
                               .content(content)
                               .reviewer(reviewer)
                               .star(star)
                               .product(product)
                               .build();
    }

}









