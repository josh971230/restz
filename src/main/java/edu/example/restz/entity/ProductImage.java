package edu.example.restz.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ProductImage implements  Comparable<ProductImage> {
    private int ino;
    private String filename;


    @Override
    public int compareTo(ProductImage o) {
        return this.ino - o.ino;
    }
}
