package edu.example.restz.controller;

import edu.example.restz.dto.*;
import edu.example.restz.exception.ProductException;
import edu.example.restz.exception.ReviewException;
import edu.example.restz.service.ReviewService;
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
@RequestMapping("/api/v1/reviews")
@Log4j2
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> register(@Validated @RequestBody ReviewDTO reviewDTO,
                                              Principal principal) {
        log.info("--- register()");

        if(!principal.getName().equals( reviewDTO.getReviewer()) ) {
            throw ReviewException.NOT_MATCHED_REVIEWER.get();
        }

        return ResponseEntity.ok(reviewService.register(reviewDTO));
    }

    @GetMapping("/{rno}")
    public ResponseEntity<ReviewDTO> read(@PathVariable("rno") Long rno) {
        log.info("--- read()");
        log.info("--- rno : " + rno);
        return ResponseEntity.ok(reviewService.read(rno));
    }

    @PutMapping("/{rno}")               //수정
    public ResponseEntity<ReviewDTO> modify( @Validated @RequestBody ReviewDTO reviewDTO,
                                             Authentication authentication,
                                             @PathVariable("rno") Long rno) {
        log.info("--- modify()");

        if( !rno.equals(reviewDTO.getRno())) {  //rno가 일치하지 않는 경우
            throw ReviewException.NOT_MATCHED.get();
        }

        String inputReviewer = reviewDTO.getReviewer();
        String dbReviewer = reviewService.read(rno).getReviewer();
        if(!authentication.getName().equals(inputReviewer)||//인증 사용자, DTO의 사용자 불일치
           !dbReviewer.equals(inputReviewer)){  //
            throw ReviewException.NOT_MATCHED_REVIEWER.get();
        }

        return ResponseEntity.ok(reviewService.modify(reviewDTO));
    }

    //삭제 - 등록한 사용자와 ADMIN role이 있는 사용자는 삭제 가능하도록 처리
    @DeleteMapping("/{rno}")
    public ResponseEntity<Map<String, String>> remove(Authentication authentication,
                                                      @PathVariable("rno") Long rno) {
        log.info("--- remove()");

        String mid = reviewService.read(rno).getReviewer();
        if(!authentication.getName().equals(mid)) {  //등록한 사용자가 아닌 경우
            Collection<? extends GrantedAuthority> authorities
                    = authentication.getAuthorities();   // ADMIN role이 없으면 예외 발생

            authorities.stream()
                    .filter( auth -> auth.getAuthority().equals("ROLE_ADMIN") )
                    .findAny().orElseThrow(ReviewException.NOT_MATCHED_REVIEWER::get);
        }
        reviewService.remove(rno);        //삭제 처리 후
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    //목록
    //기본요청 - 1 페이지, 리뷰 5개
    //페이지 번호를 음수로 지정 : 1 이상이어야 합니다
    //사이즈를 5 미만으로 지정 : 5 이상이어야 합니다    //
    @GetMapping("/list/{pno}")
    public ResponseEntity<Page<ReviewDTO>> getList(
                                            @PathVariable("pno") Long pno,
                                            @Validated ReviewPageRequestDTO pageRequestDTO){
        log.info("getList() ----- " + pageRequestDTO);         //로그로 출력
        pageRequestDTO.setPno(pno);
        return ResponseEntity.ok(reviewService.getList(pageRequestDTO));
    }
}
