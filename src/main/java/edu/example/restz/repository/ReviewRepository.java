package edu.example.restz.repository;

import edu.example.restz.dto.ProductDTO;
import edu.example.restz.dto.ReviewDTO;
import edu.example.restz.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    //tbl_review와 tbl_product를 조인하여 Review 반환
    @Query("SELECT r FROM Review r JOIN FETCH r.product WHERE r.rno = :rno")
    Optional<Review> getReviewProd(@Param("rno") Long rno);

    @Query(" SELECT r FROM Review r " +
           " JOIN FETCH r.product rp " +
           " JOIN FETCH rp.images WHERE r.rno = :rno ")
    Optional<Review> getReviewProdImg(@Param("rno") Long rno);

    @Query("SELECT r FROM Review r WHERE r.product.pno = :pno")
    Page<ReviewDTO> list(@Param("pno") Long pno, Pageable pageable);
}
















