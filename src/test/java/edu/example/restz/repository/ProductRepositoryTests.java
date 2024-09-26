package edu.example.restz.repository;

import edu.example.restz.dto.ProductDTO;
import edu.example.restz.dto.ProductListDTO;
import edu.example.restz.dto.TodoDTO;
import edu.example.restz.entity.Product;
import edu.example.restz.entity.ProductImage;
import edu.example.restz.entity.Todo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Log4j2
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository productRepository;

    @Test   //insert 테스트
    public void testInsert(){
        //GIVEN - Product 엔티티 객체 생성
        IntStream.rangeClosed(1, 50).forEach(i -> {
            Product product = Product.builder()
                                     .pname("신규 상품_" + i)
                                     .price(5000)
                                     .description("상품 설명")
                                     .registerId("user5")
                                     .build();

            product.addImage(i + "_image1.jpg");
            product.addImage(i + "_image2.jpg");

            //WHEN - 엔티티 저장
            Product savedProduct = productRepository.save(product);

            //THEN - savedProduct가 널이 아니고 mno는 1일 것
            assertNotNull(savedProduct);
            assertEquals(i, savedProduct.getPno());
            assertEquals(0, savedProduct.getImages().first().getIno());
        });
    }

    @Test
    @Transactional(readOnly = true) //읽기 전용 트랜잭션 모드 설정
    public void testRead(){
        Long pno = 1L;

        Optional<Product> foundProduct = productRepository.findById(pno);
        assertTrue(foundProduct.isPresent(), "Product should be present");

        System.out.println("-----------------------");
        Product product = foundProduct.get();
        SortedSet<ProductImage> productImages = product.getImages(); //1. foundProduct 객체에서 ProductImage 객체 Set을 가져와서 productImages에 저장
        assertNotNull(productImages);       //2. productImages 널이 아닌지 검증
        assertEquals(0, productImages.first().getIno()); //3. productImages의 첫번째-first()의 ino가 0과 같은지 검증
    }

    @Test
    public void testGetProduct(){
        Long pno = 1L;

        Optional<Product> foundProduct = productRepository.getProduct(pno);
        assertTrue(foundProduct.isPresent(), "Product should be present");
        log.info(foundProduct);

        System.out.println("-----------------------");
        Product product = foundProduct.get();
        SortedSet<ProductImage> productImages = product.getImages(); //1. foundProduct 객체에서 ProductImage 객체 Set을 가져와서 productImages에 저장
        assertNotNull(productImages);       //2. productImages 널이 아닌지 검증
        assertEquals(0, productImages.first().getIno()); //3. productImages의 첫번째-first()의 ino가 0과 같은지 검증

        log.info(productImages);
    }

    @Test
    @Transactional
    @Commit
    public void testUpdate(){
        Long pno = 1L;
        String pname = "변경 상품";
        int price = 1000;
        String img1 = "new1.jpg";
        String img2 = "new2.jpg";

        Optional<Product> foundProduct = productRepository.getProduct(pno); //1. pno에 해당하는 데이터 가져오기
        assertTrue(foundProduct.isPresent(), "Product should be present");  //2. 1의 데이터가 존재하는지 검증

        Product product = foundProduct.get();   //3. 1의 데이터에서 Product 객체 가져오기
        product.changePname(pname);             //4. 3의 객체에  pname, price는 변경하고 img1, img2는 추가
        product.changePrice(price);
        product.addImage(img1);
        product.addImage(img2);

        foundProduct = productRepository.getProduct(pno);   //5. pno에 해당하는 데이터 다시 가져오기
        assertEquals(pname, foundProduct.get().getPname()); //6. pname과 5의 상품이름이 일치하는지 검증
        assertEquals(price, foundProduct.get().getPrice()); //7. price와 5의 상품가격이 일치하는지 검증

        SortedSet<ProductImage> productImages = product.getImages();    //8. 5의 데이터에서 ProductImage 객체 Set을 가져와서 productImages에 저장
        assertEquals(3, productImages.last().getIno());   //9. 8의 마지막 데이터의 ino가 3과 같은지 검증
    }

    @Test
    @Transactional
    @Commit
    public void testDelete() {
        Long pno = 5L;

        assertTrue(productRepository.getProduct(pno).isPresent(),
                   "Product should be present");  //1. pno에 해당하는 Product 객체가 존재하는지 검증

        productRepository.deleteById(pno); //2. pno에 해당하는 Product 객체 삭제

        assertFalse(productRepository.getProduct(pno).isPresent(),
                   "Product should be present");    //3. pno에 해당하는 Product 객체가 존재하지 않는 것을 검증
    }

    @Test
    public void testGetProductDTO(){     //2. ProductRepository의 getProductDTO 메서드 테스트
        Long pno = 1L;                   //2.0 수정 테스트에서 사용한 상품번호를 이용하여

        Optional<ProductDTO> foundProductDTO = productRepository.getProductDTO(pno);
        assertTrue(foundProductDTO.isPresent(),      //2.1 반환결과가 존재하는지 검증
                   "ProductDTO should be present");

        List<String> images = foundProductDTO.get().getImages();
        assertNotNull(images);                      //2.2 이미지 목록이 널이 아닌지 검증
        assertEquals("new2.jpg", images.get(3));    //2.3 이미지 목록의 3번째 인덱스의 파일명이 new2.jpg와 같은지 검증
        log.info(foundProductDTO);
    }


    /*
    Optional[Product(pno=1, pname=변경 상품, price=1000,
                    description=상품 설명, registerId=user5,
                    regDate=2024-08-30T11:09:26.203212,
                    modDate=2024-08-30T14:18:03.452406)]
-----------------------
                    [ProductImage(ino=0, filename=1_image1.jpg),
                     ProductImage(ino=1, filename=1_image2.jpg),
                     ProductImage(ino=2, filename=new1.jpg),
                     ProductImage(ino=3, filename=new2.jpg),
                     ProductImage(ino=4, filename=new1.jpg),
                     ProductImage(ino=5, filename=new2.jpg)]    */

//    Optional[ProductDTO(pno=1, pname=변경 상품, price=1000,
    //                    description=상품 설명, registerId=user5,
    //                    images=[1_image1.jpg, 1_image2.jpg, new1.jpg,
    //                            new2.jpg, new1.jpg, new2.jpg])]

//  ProductListDTO   pno, pname,   price, registerId, pimage
//                     1  변경 상품  1000   user5      1_image1.jpg
//                     2  신규 상품  5000   user5      1_image1.jpg
//                     3  신규 상품  5000   user5      1_image1.jpg

    @Test   //페이징 테스트
    public void testList(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());

        Page<ProductListDTO> productList = productRepository.list(pageable);
        assertNotNull( productList );
        assertEquals(51, productList.getTotalElements()); //전체 게시물 수
        assertEquals(6, productList.getTotalPages());     //총 페이지 수
        assertEquals(0,  productList.getNumber()) ;        //현재 페이지 번호 0
        assertEquals(10, productList.getSize());           //한 페이지 게시물 수 10
        assertEquals(10, productList.getContent().size()); //      "

        productList.getContent().forEach(System.out::println);
    }

    @Test   //페이징 테스트
    @Transactional
    public void testListWithAllImages(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());

        Page<ProductDTO> productList = productRepository.listWithAllImages(pageable);
        assertNotNull( productList );
        assertEquals(49, productList.getTotalElements()); //전체 게시물 수
        assertEquals(5, productList.getTotalPages());     //총 페이지 수
        assertEquals(0,  productList.getNumber()) ;        //현재 페이지 번호 0
        assertEquals(10, productList.getSize());           //한 페이지 게시물 수 10
        assertEquals(10, productList.getContent().size()); //      "

        productList.getContent().forEach(System.out::println);
    }

    @Test   //페이징 테스트
    public void testListWithAllImagesFetch(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());

//        Page<ProductDTO> productList = productRepository.listWithAllImagesFetch(pageable);
        Page<ProductDTO> productList = productRepository.getProductDTOFetch(pageable);
        assertNotNull( productList );
        assertEquals(102, productList.getTotalElements()); //전체 상품이미지 수
        assertEquals(11, productList.getTotalPages());     //총 페이지 수
        assertEquals(0,  productList.getNumber()) ;        //현재 페이지 번호 0
        assertEquals(10, productList.getSize());           //한 페이지 게시물 수 10
        assertEquals(10, productList.getContent().size()); //      "

        productList.getContent().forEach(System.out::println);
    }

    @Test   //페이징 테스트 - 리뷰 개수 포함
    public void testListWithReviewCount(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").ascending());

        Page<ProductListDTO> productList = productRepository.listWithReviewCount(pageable);
        assertNotNull( productList );
        assertEquals(51, productList.getTotalElements()); //전체 게시물 수
        assertEquals(6, productList.getTotalPages());     //총 페이지 수
        assertEquals(0,  productList.getNumber()) ;        //현재 페이지 번호 0
        assertEquals(10, productList.getSize());           //한 페이지 게시물 수 10
        assertEquals(10, productList.getContent().size()); //      "

        productList.getContent().forEach(System.out::println);
    }
}











