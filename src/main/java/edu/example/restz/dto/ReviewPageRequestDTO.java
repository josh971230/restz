package edu.example.restz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewPageRequestDTO {
    @Builder.Default
    @Min(1)
    private int page = 1;   //페이지 번호 - 첫번째 페이지 0부터 시작

    @Builder.Default
    @Min(5)
    @Max(100)
    private int size = 5;  //한 페이지 게시물 수

    private Long pno;

    public Pageable getPageable(Sort sort){  //페이지번호, 페이지 게시물 수, 정렬 순서를 Pageable 객체로 반환
        return PageRequest.of(page - 1, size, sort);
    }
}
