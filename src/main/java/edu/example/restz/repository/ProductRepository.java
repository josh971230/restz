package edu.example.restz.repository;

import edu.example.restz.dto.ProductDTO;
import edu.example.restz.entity.Product;
import edu.example.restz.entity.ProductImage;
import edu.example.restz.repository.search.ProductSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {
//    @EntityGraph(attributePaths = {"images"},
//                 type= EntityGraph.EntityGraphType.FETCH)
//    @Query("SELECT p FROM Product p WHERE p.pno = :pno")
//    Optional<Product> getProduct(@Param("pno") Long pno);

    @Query("SELECT p FROM Product p JOIN FETCH p.images pi WHERE p.pno = :pno")
    Optional<Product> getProduct(@Param("pno") Long pno);

    //1. tbl_product와 tbl_product_image를 조인하여
    //   지정된 상품번호의 ProductDTO를 반환하는 getProductDTO 메서드
    @Query("SELECT p FROM Product p JOIN FETCH p.images pi WHERE p.pno = :pno")
    Optional<ProductDTO> getProductDTO(@Param("pno") Long pno);

    @Query("SELECT p FROM Product p JOIN FETCH p.images pi")
    Page<ProductDTO> getProductDTOFetch(Pageable pageable);
}











