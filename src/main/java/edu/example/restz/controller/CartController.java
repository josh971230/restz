package edu.example.restz.controller;

import edu.example.restz.dto.CartItemDTO;
import edu.example.restz.dto.ReviewDTO;
import edu.example.restz.dto.ReviewPageRequestDTO;
import edu.example.restz.exception.CartException;
import edu.example.restz.exception.ReviewException;
import edu.example.restz.service.CartService;
import edu.example.restz.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
@Log4j2
public class CartController {
    private final CartService cartService;

    @PostMapping
    @PreAuthorize("authentication.name == #cartItemDTO.customer")
    public ResponseEntity<List<CartItemDTO>> add(@RequestBody CartItemDTO cartItemDTO) {
        log.info("--- add()");

        cartService.add(cartItemDTO);

        return ResponseEntity.ok(cartService.getAllItems(cartItemDTO.getCustomer()));
    }

    @GetMapping("/{cno}")
    public ResponseEntity<List<CartItemDTO>> show(@PathVariable("cno") Long cno,
                                                  Principal principal) {
        log.info("--- show()");
        String mid = principal.getName();

        //로그인한 사용자의 장바구니가 아니거나, 장바구니가 없으면 Cart NOT FOUND 예외 던지고
        cartService.checkCartCustomer(mid, cno);

        return ResponseEntity.ok(cartService.getAllItems(mid));
    }

    @PutMapping("/{itemNo}")               //수정
    public ResponseEntity<List<CartItemDTO>>  modify( @RequestBody CartItemDTO cartItemDTO,
                                                     Authentication authentication,
                                                     @PathVariable("itemNo") Long itemNo) {
        log.info("--- modify()");

        if( !itemNo.equals(cartItemDTO.getItemNo())) {  //itemNo가 일치하지 않는 경우
            throw CartException.NOT_MATCHED_CARTITEM.get();
        }

        String mid = authentication.getName();

        //로그인한 사용자의 장바구니 아이템이 아니면 Customer NOT Matched 예외 던지기

        cartService.modify(cartItemDTO);
        return ResponseEntity.ok(cartService.getAllItems(mid));
    }

    @DeleteMapping("/{itemNo}")
    public ResponseEntity<List<CartItemDTO>> remove(@PathVariable("itemNo") Long itemNo,
                                                    Principal principal) {
        log.info("--- remove()");
        String mid = principal.getName();

        //로그인한 사용자의 장바구니 아이템이 아닌지 확인
        cartService.checkItemCustomer(mid, itemNo);
        cartService.modify(CartItemDTO.builder().itemNo(itemNo)
                                                .quantity(0).build());

        return ResponseEntity.ok(cartService.getAllItems(mid));
    }

}
