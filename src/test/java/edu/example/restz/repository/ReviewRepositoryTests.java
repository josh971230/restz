package edu.example.restz.repository;

import edu.example.restz.dto.ProductListDTO;
import edu.example.restz.dto.ReviewDTO;
import edu.example.restz.entity.Product;
import edu.example.restz.entity.ProductImage;
import edu.example.restz.entity.Review;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.SortedSet;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Log4j2
public class ReviewRepositoryTests {
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void testInsert() {
        //tbl_review 테이블에 상품번호 1번의 테스트 데이터 10개 추가
        Long pno = 1L;

        Product product = Product.builder().pno(pno).build();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            Review review = Review.builder()
                                    .content("리뷰 테스트" + i)
                                    .reviewer("user" + i)
                                    .star(5)
                                    .product(product)
                                    .build();

            Review savedReview = reviewRepository.save(review);  //리뷰를 테이블에 저장하고
            assertNotNull(savedReview); //저장된 객체가 널이 아닌지 검증하고
            assertEquals(i, savedReview.getRno());//저장된 객체의 rno가 i와 같은지 검증
        });
    }

    //리뷰 번호 5번 조회
    //조회 결과가 존재하는지 검증
    //조회 결과에서 Product 객체를 받아오고
    //Product 객체가 널이 아닌지 검증
    @Test
    @Transactional(readOnly = true) //읽기 전용 트랜잭션 모드 설정
    public void testRead() {
        Long rno = 5L;

        Optional<Review> foundReview = reviewRepository.findById(rno);
        assertTrue(foundReview.isPresent(), "foundReview should be present");

        System.out.println("-----------------------");
        Product product = foundReview.get().getProduct();
        assertNotNull(product);
        assertEquals(1, product.getPno());
        assertEquals(1000, product.getPrice());
    }

    @Test
    public void testGetReviewProd() {
        //페치 조인 테스트
        Long rno = 5L;

        Optional<Review> foundReview = reviewRepository.getReviewProd(rno);
        assertTrue(foundReview.isPresent(), "foundReview should be present");

        System.out.println("-----------------------");
        Product product = foundReview.get().getProduct();
        assertNotNull(product);
        assertEquals(1, product.getPno());
        assertEquals(1000, product.getPrice());
    }

    @Test
    public void testGetReviewProdImg() {
        //리뷰 조회 시 상품과 상품 이미지도 가져오기 테스트
        Long rno = 5L;

        Optional<Review> foundReview
                = reviewRepository.getReviewProdImg(rno);
        assertTrue(foundReview.isPresent(), "foundReview should be present");

        System.out.println("-----------------------");
        Product product = foundReview.get().getProduct();
        assertNotNull(product);
        assertEquals(1, product.getPno());
        assertEquals(1000, product.getPrice());
        assertEquals(0, product.getImages().first().getIno());
    }

    @Test
    @Transactional
    @Commit
    public void testUpdate(){
        //given
        Long rno = 5L;
        String content = "리뷰 수정 테스트";
        int star = 1;

        //when  //rno에 해당하는 리뷰 조회, 리뷰 결과가 존재하는지 검증
        Optional<Review> foundReview = reviewRepository.findById(rno);
        assertTrue(foundReview.isPresent(), "foundReview should be present");

        Review review = foundReview.get();
        review.changeContent(content); //수정 처리
        review.changeStar(star);

        //then  //rno에 해당하는 리뷰 재조회, content와 star가 주어진 값과 일치하는지 검증
        foundReview = reviewRepository.findById(rno);
        assertEquals(content, foundReview.get().getContent());
        assertEquals(star, foundReview.get().getStar());
    }

    @Test
    public void testDeleteById(){
        //given
        Long rno = 4L;

        //when  //rno에 해당하는 리뷰가 존재하는지 검증
        assertTrue(reviewRepository.findById(rno).isPresent(),
                   "foundReview should be present");

        reviewRepository.deleteById(rno);   //rno에 해당하는 리뷰 삭제

        //then  //rno에 해당하는 리뷰가 존재하지 않는지 검증
        assertFalse(reviewRepository.findById(rno).isPresent(),
                    "foundReview should not be present");
    }

    @Test
    public void testList(){
        //given
        Long pno = 1L;

        Pageable pageable = PageRequest.of(0, 5, Sort.by("rno").ascending());    //한 페이지에 5개씩, 첫번째 페이지를 오름차순 정렬로 가져오기

        Page<ReviewDTO> reviewList = reviewRepository.list(pno, pageable);
        assertNotNull( reviewList ); //결과가 널이 아님 검증
        assertEquals(10, reviewList.getTotalElements()); //전체 리뷰 수
        assertEquals(2, reviewList.getTotalPages());     //총 페이지 수
        assertEquals(0,  reviewList.getNumber()) ;        //현재 페이지 번호 0
        assertEquals(5, reviewList.getSize());           //한 페이지 게시물 수 5
        assertEquals(5, reviewList.getContent().size()); //      "

        reviewList.getContent().forEach(System.out::println);
    }
}













