package edu.example.restz.repository.search;

import edu.example.restz.dto.ProductDTO;
import edu.example.restz.dto.ProductListDTO;
import edu.example.restz.dto.TodoDTO;
import edu.example.restz.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductSearch {
    Page<ProductListDTO> list(Pageable pageable);
    Page<ProductListDTO> listWithReviewCount(Pageable pageable);
    Page<ProductDTO> listWithAllImages(Pageable pageable);
    Page<ProductDTO> listWithAllImagesFetch(Pageable pageable);
}
