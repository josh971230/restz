package edu.example.restz.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name="tbl_review",
       indexes=@Index(columnList = "product_pno"))
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String content;
    private String reviewer;
    private int star;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_pno")
    private Product product;

    @CreatedDate
    private LocalDateTime regDate;

    @LastModifiedDate
    private LocalDateTime modDate;

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeStar(int star) {
        this.star = star;
    }
}
