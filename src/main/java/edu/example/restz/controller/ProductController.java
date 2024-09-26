package edu.example.restz.controller;

import edu.example.restz.dto.PageRequestDTO;
import edu.example.restz.dto.ProductDTO;
import edu.example.restz.dto.ProductListDTO;
import edu.example.restz.dto.TodoDTO;
import edu.example.restz.exception.ProductException;
import edu.example.restz.service.ProductService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Log4j2
public class ProductController {
    private  final ProductService productService;

    @GetMapping("/{pno}")
    public ResponseEntity<ProductDTO> read(@PathVariable("pno") Long pno) {
        log.info("--- read()");
        log.info("--- pno : " + pno);
        return ResponseEntity.ok(productService.read(pno));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> register(@Validated @RequestBody ProductDTO productDTO,
                                               Principal principal) {
        log.info("--- register()");
        log.info("--- productDTO : " + productDTO);
        log.info("--- principal : " + principal);
        log.info("--- principal.getName() : " + principal.getName());

        if(productDTO.getImages() == null || productDTO.getImages().isEmpty()) {  //이미지가 없는 경우
            throw ProductException.NO_IMAGE.get();      // NO Product Image를 예외 메시지로 ProductTaskException 예외 발생 시키기
        }

        if(!principal.getName().equals( productDTO.getRegisterId()) ) {  //인증된 사용자와 productDTO의 등록자가 일치하지 않는 경우
            throw ProductException.REGISTER_ERR.get();   //NO Authenticated user를 예외 메시지로 ProductTaskException 예외 발생 시키기
        }

        return ResponseEntity.ok(productService.register(productDTO)); //상태 코드를 200 OK로 하여, 상품 등록 서비스가 반환하는 데이터를 뷰로 전달
    }

    @PutMapping("/{pno}")               //수정
    public ResponseEntity<ProductDTO> modify(@Validated @RequestBody ProductDTO productDTO,
                                             Authentication authentication,
                                             @PathVariable("pno") Long pno) {
        log.info("--- modify()");
        log.info("--- productDTO : " + productDTO);
        log.info("--- authentication : " + authentication);
        log.info("--- authentication.getName() : " + authentication.getName());


        if( !pno.equals(productDTO.getPno())) {  //pno가 일치하지 않는 경우
            throw ProductException.NOT_FOUND.get();
        }

        if(productDTO.getImages() == null || productDTO.getImages().isEmpty()) {  //이미지가 없는 경우
            throw ProductException.NO_IMAGE.get();      // NO Product Image를 예외 메시지로 ProductTaskException 예외 발생 시키기
        }

        String mid = productService.read(productDTO.getPno()).getRegisterId();
        if(!authentication.getName().equals( productDTO.getRegisterId()) || //인증된 사용자와 productDTO의 등록자가 일치하지 않는 경우
           !mid.equals(productDTO.getRegisterId())){
            throw ProductException.REGISTER_ERR.get();   //NO Authenticated user를 예외 메시지로 ProductTaskException 예외 발생 시키기
        }

        return ResponseEntity.ok(productService.modify(productDTO));
    }

    //삭제 - 등록한 사용자와 ADMIN role이 있는 사용자는 삭제 가능하도록 처리
    @DeleteMapping("/{pno}")
    public ResponseEntity<Map<String, String>> remove( Authentication authentication,
                                                       @PathVariable("pno") Long pno) {
        log.info("--- remove()");
        log.info("--- pno : " + pno);

        String mid = productService.read(pno).getRegisterId();
        if(!authentication.getName().equals(mid)) {  //등록한 사용자가 아닌 경우
            Collection<? extends GrantedAuthority> authorities
                        = authentication.getAuthorities();   // ADMIN role이 없으면 예외 발생

            authorities.stream()
                       .filter( auth -> auth.getAuthority().equals("ROLE_ADMIN") )
                       .findAny().orElseThrow(ProductException.REGISTER_ERR::get);
        }
        productService.remove(pno);        //삭제 처리 후
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    //상품 목록 /api/v1/products
        //기본 요청    - 1page 10개
        //2page 요청  - 2page 10개
        //-2page 요청 - page : "1 이상이어야 합니다"
        //2page size 5 요청  - size : "10 이상이어야 합니다"
        //Apage 요청  - page : "Failed to convert property value of type 'java.lang.String'...
    @GetMapping                         //목록
    public ResponseEntity<Page<ProductListDTO>> getList(@Validated PageRequestDTO pageRequestDTO){
        log.info("getList() ----- " + pageRequestDTO);         //로그로 출력
        return ResponseEntity.ok(productService.getList(pageRequestDTO));
    }
}









