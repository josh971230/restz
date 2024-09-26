package edu.example.restz.service;

import edu.example.restz.dto.PageRequestDTO;
import edu.example.restz.dto.TodoDTO;
import edu.example.restz.entity.Todo;
import edu.example.restz.exception.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

@SpringBootTest
@Log4j2
public class TodoServiceTests {
    @Autowired                       //1. 필드 인젝션 어노ㅌㅔ이션
    private TodoService todoService; //2. 객체 선언

    @Test                           //3. Junit 테스트를 위한 어노테이션
    public void testRegister(){
        TodoDTO todoDTO = new TodoDTO();     //4. TodoDTO 객체를 생성하여
        todoDTO.setTitle("SERVICE TEST");//   임의의 데이터를 저장한 후
        todoDTO.setWriter("SERVER");
        todoDTO.setDueDate(LocalDate.of(2024, 12, 31));

        TodoDTO savedTodo = todoService.register(todoDTO);   //5. 데이터베이스에 저장하는 메서드를 호출하고 반환되는 값을 저장
        assertNotNull(savedTodo);                           //6. 반환된 결과가 null이 아닌지 검증
        assertEquals("SERVER", savedTodo.getWriter());      //7. 반환된 결과의 writer가 4에서 지정한 값과 같은지 검증
        log.info(savedTodo);                                //8. 반환된 객체를 info 레벨의 로그로 출력
    }

    @Test
    public void testRead(){
        Long mno = 222L;                            //given
        TodoDTO todoDTO = todoService.read(mno);    //when
        assertNotNull(todoDTO);                     //then
        assertEquals(mno, todoDTO.getMno());

        log.info(todoDTO);
    }

    @Test
    public void testRemove(){
        try {
            Long mno = 4L;              //given - 4번 글
            todoService.remove(mno);    //when  - 4번 글 삭제
        } catch(EntityNotFoundException e ) {
            log.info("EntityNotFoundException message " + e.getMessage());
            //then  - 4번 최초 삭제 시에는 예외 X
            //        삭제 된 이후 재시도 시 예외코드가 404와 같은지 확인
            assertEquals(404, e.getCode());
        }
    }

    @Test
    public void testModify(){
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setMno(5L);
        todoDTO.setTitle("SERVICE MODIFIED");
        todoDTO.setWriter("modifier");
        todoDTO.setDueDate(LocalDate.of(2024, 11, 30));

        TodoDTO modifyTodo = todoService.modify(todoDTO);
        assertNotNull(modifyTodo);
        assertEquals("SERVICE MODIFIED", modifyTodo.getTitle());
        assertEquals("modifier", modifyTodo.getWriter());
        log.info(modifyTodo);
    }

    @Test
    public void testGetList(){
        PageRequestDTO pageRequestDTO = new PageRequestDTO(); //기본생성자 - 1 페이지, 게시물 10개씩

        Page<TodoDTO> todoPage = todoService.getList(pageRequestDTO);
        assertNotNull( todoPage );
        assertEquals(100, todoPage.getTotalElements()); //전체 게시물 수 100개
        assertEquals(10, todoPage.getTotalPages());     //총 페이지 수 10개
        assertEquals(0,  todoPage.getNumber()) ;        //현재 페이지 번호 0
        assertEquals(10, todoPage.getSize());           //한 페이지 게시물 수 10
        assertEquals(10, todoPage.getContent().size()); //      "

        todoPage.getContent().forEach(System.out::println);
    }
}
