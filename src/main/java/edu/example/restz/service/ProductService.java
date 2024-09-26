package edu.example.restz.service;
import edu.example.restz.dto.PageRequestDTO;
import edu.example.restz.dto.ProductDTO;
import edu.example.restz.dto.ProductListDTO;
import edu.example.restz.dto.TodoDTO;
import edu.example.restz.entity.Product;
import edu.example.restz.entity.Todo;
import edu.example.restz.exception.EntityNotFoundException;
import edu.example.restz.exception.ProductException;
import edu.example.restz.exception.ProductTaskException;
import edu.example.restz.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
public class ProductService {
    private final ProductRepository productRepository;

    //상품 등록
    public ProductDTO register(ProductDTO productDTO){   //등록
        try {
            Product product = productDTO.toEntity();
            productRepository.save(product);
            return new ProductDTO(product);
        } catch(Exception e) {                  //상품 등록 시 예외가 발생한 경우
            log.error("--- " + e.getMessage()); //에러 로그로 발생 예외의 메시지를 기록하고
            throw ProductException.NOT_REGISTERED.get();  //예외 메시지를 Product NOT Registered로 지정하여 ProductTaskException 발생시키기
        }
    }

    public ProductDTO read(Long pno) {     //상품 조회
        Optional<ProductDTO> productDTO = productRepository.getProductDTO(pno);
        return productDTO.orElseThrow(ProductException.NOT_FOUND::get);
    }

    //상품 수정
    public ProductDTO modify(ProductDTO productDTO){
        Optional<Product> foundProduct = productRepository.findById(productDTO.getPno());   //수정하려는 상품을 데이터베이스에서 조회해서
        Product product = foundProduct.orElseThrow(ProductException.NOT_FOUND::get);

        try {
            //필요한 부분 수정 - 변경이 감지되면 수정 처리 수행
            product.changePname(productDTO.getPname()); //상품 이름, 가격, 설명 수정
            product.changePrice(productDTO.getPrice());
            product.changeDescription(productDTO.getDescription());

            product.clearImages();        //기존 이미지 삭제
            List<String> images = productDTO.getImages();//새 이미지 목록을 가져와서
            if (images != null && !images.isEmpty()) {
                images.forEach(product::addImage);   // 추가
            }

            return new ProductDTO(product); //변경된 상품을 반환
        } catch(Exception e) {
            log.error("--- " + e.getMessage());
            throw ProductException.NOT_MODIFIED.get();
        }
    }

    //상품 삭제
    public void remove(Long pno){
        Optional<Product> foundProduct = productRepository.findById(pno);   //수정하려는 상품을 데이터베이스에서 조회해서
        Product product = foundProduct.orElseThrow(ProductException.NOT_FOUND::get);

        try {
            productRepository.delete(product);
        } catch(Exception e) {
            log.error("--- " + e.getMessage());
            throw ProductException.NOT_REMOVED.get();
        }
    }

    //상품 목록
    public Page<ProductListDTO> getList(PageRequestDTO pageRequestDTO) { //목록
        try {
            Sort sort = Sort.by("pno").descending();
            Pageable pageable = pageRequestDTO.getPageable(sort);
            return productRepository.list(pageable );
        } catch(Exception e) {
            log.error("--- " + e.getMessage());
            throw ProductException.NOT_FETCHED.get();
        }
    }
}












