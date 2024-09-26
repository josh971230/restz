package edu.example.restz.repository;

import edu.example.restz.dto.ReviewDTO;
import edu.example.restz.entity.Cart;
import edu.example.restz.entity.CartItem;
import edu.example.restz.entity.Product;
import edu.example.restz.entity.Review;
import edu.example.restz.exception.CartException;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Log4j2
public class CartRepositoryTests {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    public void testInsert() {
        Long pno = 52L;
        String mid = "user9";
        int quantity = 1;

        Optional<Cart> foundCart = cartRepository.findByCustomer(mid);
        Cart savedCart = foundCart.orElseGet(() -> { //장바구니가 없으면 생성하여 반환
                                Cart cart = Cart.builder().customer(mid).build();
                                return cartRepository.save(cart);
                         });
        Product product = Product.builder().pno(pno).build();

        CartItem cartItem = CartItem.builder()
                                    .quantity(quantity)
                                    .product(product)
                                    .cart(savedCart)
                                    .build();

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        assertNotNull(savedCartItem); //저장된 객체가 널이 아닌지 검증하고
        assertEquals(3, savedCartItem.getItemNo());//저장된 객체의 rno가 i와 같은지 검증
        assertEquals(pno, savedCartItem.getProduct().getPno());
    }

    @Test
    public void testGetCartItems() {
        String customer = "user9";

        List<CartItem> cartItemList = cartItemRepository.getCartItems(customer).orElse(null);
        assertNotNull(cartItemList, "cartItemList should be not null");

        cartItemList.forEach( cartItem -> {
            System.out.println("-----------------------");
            System.out.println(cartItem);
            System.out.println(cartItem.getProduct());
            System.out.println(cartItem.getProduct().getImages());
        });
    }

    @Test
    @Transactional
    @Commit
    public void testUpdate(){
        //given
        Long itemNo = 3L;
        int quantity = 10;

        //when
        Optional<CartItem> foundCartItem = cartItemRepository.findById(itemNo);
        assertTrue(foundCartItem.isPresent(), "foundCartItem should be present");

        CartItem cartItem  = foundCartItem.get();
        cartItem.changeQuantity(quantity); //수정 처리

        //then  //rno에 해당하는 리뷰 재조회, content와 star가 주어진 값과 일치하는지 검증
        foundCartItem = cartItemRepository.findById(itemNo);
        assertEquals(quantity, foundCartItem.get().getQuantity());
    }

//    @Test
//    public void testDeleteById(){
//        //given
//        Long rno = 4L;
//
//        //when  //rno에 해당하는 리뷰가 존재하는지 검증
//        assertTrue(reviewRepository.findById(rno).isPresent(),
//                   "foundReview should be present");
//
//        reviewRepository.deleteById(rno);   //rno에 해당하는 리뷰 삭제
//
//        //then  //rno에 해당하는 리뷰가 존재하지 않는지 검증
//        assertFalse(reviewRepository.findById(rno).isPresent(),
//                    "foundReview should not be present");
//    }
//
//    @Test
//    public void testList(){
//        //given
//        Long pno = 1L;
//
//        Pageable pageable = PageRequest.of(0, 5, Sort.by("rno").ascending());    //한 페이지에 5개씩, 첫번째 페이지를 오름차순 정렬로 가져오기
//
//        Page<ReviewDTO> reviewList = reviewRepository.list(pno, pageable);
//        assertNotNull( reviewList ); //결과가 널이 아님 검증
//        assertEquals(10, reviewList.getTotalElements()); //전체 리뷰 수
//        assertEquals(2, reviewList.getTotalPages());     //총 페이지 수
//        assertEquals(0,  reviewList.getNumber()) ;        //현재 페이지 번호 0
//        assertEquals(5, reviewList.getSize());           //한 페이지 게시물 수 5
//        assertEquals(5, reviewList.getContent().size()); //      "
//
//        reviewList.getContent().forEach(System.out::println);
//    }
}













