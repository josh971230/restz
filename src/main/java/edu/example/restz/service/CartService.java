package edu.example.restz.service;

import edu.example.restz.dto.CartItemDTO;
import edu.example.restz.dto.ReviewDTO;
import edu.example.restz.dto.ReviewPageRequestDTO;
import edu.example.restz.entity.Cart;
import edu.example.restz.entity.CartItem;
import edu.example.restz.entity.Product;
import edu.example.restz.entity.Review;
import edu.example.restz.exception.CartException;
import edu.example.restz.exception.ReviewException;
import edu.example.restz.repository.CartItemRepository;
import edu.example.restz.repository.CartRepository;
import edu.example.restz.repository.ProductRepository;
import edu.example.restz.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public void add(CartItemDTO cartItemDTO){   //등록
        Optional<Cart> foundCart = cartRepository.findByCustomer(cartItemDTO.getCustomer());

        //장바구니 유무 확인
        Cart savedCart = foundCart.orElseGet(() -> { //장바구니가 없으면 생성하여 반환
            Cart cart = Cart.builder().customer(cartItemDTO.getCustomer()).build();
            return cartRepository.save(cart);
        });

        //상품 유무 확인
        Product foundProduct = productRepository.findById(cartItemDTO.getPno())
                                                .orElseThrow(
                                        CartException.NOT_FOUND_PRODUCT::get);   //없으면 예외 던지기

        CartItem cartItem = CartItem.builder()
                                    .quantity(cartItemDTO.getQuantity())
                                    .product(foundProduct)
                                    .cart(savedCart)
                                    .build();

        try {
            cartItemRepository.save(cartItem); //장바구니에 담기
        } catch(Exception e) {
            log.error("--- " + e.getMessage()); //에러 로그로 발생 예외의 메시지를 기록하고
            throw CartException.FAIL_ADD.get();
        }
    }

    public void checkCartCustomer(String customer, Long cno){
        Cart foundCart = cartRepository.findByCustomer(customer)
                                       .orElseThrow(
                                               CartException.NOT_FOUND_CART::get);

        if(!foundCart.getCno().equals(cno)) {
            throw CartException.NOT_FOUND_CART.get();
        }
    }

    public List<CartItemDTO> getAllItems(String customer) {     //조회
        List<CartItem> cartItemList = cartItemRepository.getCartItems(customer).orElse(null);

        List<CartItemDTO> itemDTOList = new ArrayList<>();
        if(cartItemList.isEmpty()) { //DB의 장바구니 목록이 비어 있으면
            return  itemDTOList;     //비어 있는 itemDTOList 반환
        }

        cartItemList.forEach(cartItem -> { //비어 있지 않으면
            //엔티티 >>> DTO 변환하여 itemDTOList 저장하여 반환
            itemDTOList.add(CartItemDTO.builder()
                                       .itemNo( cartItem.getItemNo())
                                       .pno( cartItem.getProduct().getPno())
                                       .pname( cartItem.getProduct().getPname())
                                       .price( cartItem.getProduct().getPrice())
                                       .quantity( cartItem.getQuantity())
                                       .image( cartItem.getProduct().getImages()
                                                       .first().getFilename())
                                       .build());
        });
        return itemDTOList;
    }

    public void modify(CartItemDTO cartItemDTO){    //수정
        CartItem cartItem = cartItemRepository.findById(cartItemDTO.getItemNo())
                                              .orElseThrow(
                                            CartException.NOT_FOUND_CARTITEM::get);

        if(cartItemDTO.getQuantity() <= 0) {   //수량이 0 이하면 아이템 삭제
            try {
                cartItemRepository.delete(cartItem);
                return;
            } catch(Exception e) {
                log.error("--- " + e.getMessage());
                throw CartException.FAIL_REMOVE.get();
            }
        }

        try {//필요한 부분 수정 - 변경이 감지되면 수정 처리 수행
            cartItem.changeQuantity(cartItemDTO.getQuantity());
        } catch(Exception e) {
            log.error("--- " + e.getMessage());
            throw CartException.FAIL_MODIFY.get();
        }
    }

    public void checkItemCustomer(String mid, Long itemNo) {
        String customer = cartItemRepository.getCartItemCustomer(itemNo)
                                            .orElseThrow(
                                 CartException.NOT_FOUND_CARTITEM::get);

        if(!customer.equals(mid)) {
            throw CartException.NOT_MATCHED_CUSTOMER.get();
        }
    }
}















