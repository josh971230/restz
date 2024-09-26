package edu.example.restz.service;

import edu.example.restz.dto.*;
import edu.example.restz.entity.Product;
import edu.example.restz.entity.Review;
import edu.example.restz.exception.ProductException;
import edu.example.restz.exception.ReviewException;
import edu.example.restz.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewDTO register(ReviewDTO reviewDTO){   //등록
        try {
            Review review = reviewDTO.toEntity();
            reviewRepository.save(review);
            return new ReviewDTO(review);
        } catch (DataIntegrityViolationException e){
            throw ReviewException.PRODUCT_NOT_FOUND.get();
        } catch(Exception e) {
            log.error("--- e : " + e);
            log.error("--- " + e.getMessage()); //에러 로그로 발생 예외의 메시지를 기록하고
            throw ReviewException.NOT_REGISTERED.get();
        }
    }

    public ReviewDTO read(Long rno) {     //조회
        Review review = reviewRepository.findById(rno).orElseThrow(
                                        ReviewException.NOT_FOUND::get);
        return new ReviewDTO(review);
    }

    public ReviewDTO modify(ReviewDTO reviewDTO){    //수정
        Review review = reviewRepository.findById(reviewDTO.getRno())
                                        .orElseThrow(
                                                ReviewException.NOT_FOUND::get);

        try {//필요한 부분 수정 - 변경이 감지되면 수정 처리 수행
            review.changeContent(reviewDTO.getContent());
            review.changeStar(reviewDTO.getStar());
            return new ReviewDTO(review);
        } catch(Exception e) {
            log.error("--- " + e.getMessage());
            throw ReviewException.NOT_MODIFIED.get();
        }
    }

    //삭제
    public void remove(Long rno){
        Review review = reviewRepository.findById(rno).orElseThrow(
                                        ReviewException.NOT_FOUND::get);

        try {
            reviewRepository.delete(review);
        } catch(Exception e) {
            log.error("--- " + e.getMessage());
            throw ReviewException.NOT_REMOVED.get();
        }
    }

    //목록
    public Page<ReviewDTO> getList(ReviewPageRequestDTO pageRequestDTO) {
        try {
            Sort sort = Sort.by("rno").ascending();
            Pageable pageable = pageRequestDTO.getPageable(sort);
            return reviewRepository.list(pageRequestDTO.getPno(), pageable );
        } catch(Exception e) {
            log.error("--- " + e.getMessage());
            throw ReviewException.NOT_FETCHED.get();
        }
    }
}















