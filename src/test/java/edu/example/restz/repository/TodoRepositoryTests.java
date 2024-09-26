package edu.example.restz.repository;
import edu.example.restz.dto.TodoDTO;
import edu.example.restz.entity.Todo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Log4j2
public class TodoRepositoryTests {
    @Autowired
    private TodoRepository todoRepository;

    @Test   //insert 테스트
    public void testInsert(){
        //GIVEN - Todo 엔티티 객체 생성
        Todo todo = Todo.builder().title("JPA 테스트")
                                  .writer("tester")
                                  .dueDate(LocalDate.of(2024, 8, 31))
                                  .build();
        //WHEN - 엔티티 저장
        Todo savedTodo = todoRepository.save(todo);

        //THEN - savedTodo가 널이 아니고 mno는 1일 것
        assertNotNull(savedTodo);
        assertEquals(1, savedTodo.getMno());
    }

    @Test   //테스트 데이터 100개 추가
    public void testDataInsert(){
        IntStream.rangeClosed(2, 101).forEach(i -> {
            //GIVEN - Todo 엔티티 객체 생성
            Todo todo = Todo.builder().title("Todo test " + i)
                    .writer("tester" + i)
                    .dueDate(LocalDate.of(2024, 8, 31))
                    .build();

            //WHEN - 엔티티 저장
            Todo savedTodo = todoRepository.save(todo);

            //THEN - savedTodo가 널이 아닐 것
            assertNotNull(savedTodo);
            assertEquals(i, savedTodo.getMno());
        });
    }

    @Test   //SELECT 테스트
    public void testFindById(){
        //given      //@Id 타입의 값으로 엔티티 조회
        Long mno = 1L;

        //when
        Optional<Todo> foundTodo = todoRepository.findById(mno);

        //THEN - savedTodo가 널이 아니고 mno는 1일 것
        assertNotNull(foundTodo);
        assertEquals(mno, foundTodo.get().getMno());

        log.info("foundTodo : " + foundTodo);
        log.info("mno : " + foundTodo.get().getMno());
    }

    @Test   //SELECT 테스트 - 트랜잭션 X
    public void testFindByIdNoTransactional(){
        Long mno = 1L;
        Optional<Todo> foundTodo = todoRepository.findById(mno);
        assertNotNull(foundTodo);
        assertEquals(mno, foundTodo.get().getMno());

        foundTodo = todoRepository.findById(mno);
        assertNotNull(foundTodo);
        assertEquals(mno, foundTodo.get().getMno());
    }

    @Test   //SELECT 테스트 - 트랜잭션 O
    @Transactional
    public void testFindByIdTransactional(){
        Long mno = 1L;
        Optional<Todo> foundTodo = todoRepository.findById(mno);
        assertNotNull(foundTodo);
        assertEquals(mno, foundTodo.get().getMno());

        foundTodo = todoRepository.findById(mno);
        assertNotNull(foundTodo);
        assertEquals(mno, foundTodo.get().getMno());
    }

    @Test   //UPDATE 테스트 - 트랜잭션 X
    @Commit
    public void testUpdateNoTransactional(){
        Long mno = 2L;
        Optional<Todo> foundTodo = todoRepository.findById(mno);
        foundTodo.get().changeTitle("TITLE CHANGED");
        foundTodo.get().changeWriter("CHANGER");

        assertEquals("TITLE CHANGED", foundTodo.get().getTitle());
        assertEquals("CHANGER", foundTodo.get().getWriter());

        ///////////////////////////////////////////////////////

        foundTodo = todoRepository.findById(mno);
        assertEquals("TITLE CHANGED", foundTodo.get().getTitle());
        assertEquals("CHANGER", foundTodo.get().getWriter());
    }

    @Test   //UPDATE 테스트 - 트랜잭션 O
    @Transactional
    @Commit
    public void testUpdateTransactional(){
        Long mno = 2L;
        Optional<Todo> foundTodo = todoRepository.findById(mno);
        foundTodo.get().changeTitle("TITLE CHANGED");
        foundTodo.get().changeWriter("CHANGER");

        assertEquals("TITLE CHANGED", foundTodo.get().getTitle());
        assertEquals("CHANGER", foundTodo.get().getWriter());

        ///////////////////////////////////////////////////////

        foundTodo = todoRepository.findById(mno);
        assertEquals("TITLE CHANGED", foundTodo.get().getTitle());
        assertEquals("CHANGER", foundTodo.get().getWriter());
    }

    @Test   //DELETE 테스트 - 트랜잭션 O
    @Transactional
    @Commit
    public void testDelete() {
        Long mno = 3L;
        todoRepository.deleteById(mno);

        Optional<Todo> foundTodo = todoRepository.findById(mno);
        assertTrue( foundTodo.isEmpty() );
    }

    @Test   //페이징 테스트
    public void testFindAll(){
        Pageable pageable = PageRequest.of(0,   //페이지 번호 - 첫번째 페이지 0부터 시작
                                          10,  //한 페이지 게시물 수
                                          Sort.by("mno") //게시물 정렬 기준
                                              .descending());

        Page<Todo> todoPage = todoRepository.findAll(pageable);
        assertNotNull( todoPage );
        assertEquals(100, todoPage.getTotalElements()); //전체 게시물 수 100개
        assertEquals(10, todoPage.getTotalPages());     //총 페이지 수 10개
        assertEquals(0,  todoPage.getNumber()) ;        //현재 페이지 번호 0
        assertEquals(10, todoPage.getSize());           //한 페이지 게시물 수 10
        assertEquals(10, todoPage.getContent().size()); //      "

        todoPage.getContent().forEach(System.out::println);
    }

    @Test   // @Query 테스트
    public void testListAll(){
        Pageable pageable = PageRequest.of(9,   //페이지 번호 - 첫번째 페이지 0부터 시작
                                           10);  //한 페이지 게시물 수

        Page<Todo> todoPage = todoRepository.listAll(pageable);
        assertNotNull( todoPage );
        assertEquals(100, todoPage.getTotalElements()); //전체 게시물 수 100개
        assertEquals(10, todoPage.getTotalPages());     //총 페이지 수 10개
        assertEquals(9,  todoPage.getNumber()) ;        //현재 페이지 번호 9
        assertEquals(10, todoPage.getSize());           //한 페이지 게시물 수 10
        assertEquals(10, todoPage.getContent().size()); //      "

        todoPage.getContent().forEach(System.out::println);
    }

    @Test   // Querydsl 테스트
    public void testSearch(){
        Pageable pageable = PageRequest.of(9, 10, Sort.by("mno").descending());

        Page<Todo> todoPage = todoRepository.search(pageable);
        assertNotNull( todoPage );
        assertEquals(100, todoPage.getTotalElements()); //전체 게시물 수 100개
        assertEquals(10, todoPage.getTotalPages());     //총 페이지 수 10개
        assertEquals(9,  todoPage.getNumber()) ;        //현재 페이지 번호 9
        assertEquals(10, todoPage.getSize());           //한 페이지 게시물 수 10
        assertEquals(10, todoPage.getContent().size()); //      "

        todoPage.getContent().forEach(System.out::println);
    }

    @Test
    public void testGetTodoDTO(){
        Long mno = 2L;
        Optional<TodoDTO> foundTodoDTO
                = todoRepository.getTodoDTO(mno);

        assertNotNull(foundTodoDTO);
        assertEquals("CHANGER", foundTodoDTO.get().getWriter());

        foundTodoDTO.ifPresent(System.out::println);
    }

    @Test   // DTO Projections 테스트
    public void testSearchDTO(){
        Pageable pageable = PageRequest.of(9, 10, Sort.by("mno").descending());

        Page<TodoDTO> todoPage = todoRepository.searchDTO(pageable);
        assertNotNull( todoPage );
        assertEquals(100, todoPage.getTotalElements()); //전체 게시물 수 100개
        assertEquals(10, todoPage.getTotalPages());     //총 페이지 수 10개
        assertEquals(9,  todoPage.getNumber()) ;        //현재 페이지 번호 9
        assertEquals(10, todoPage.getSize());           //한 페이지 게시물 수 10
        assertEquals(10, todoPage.getContent().size()); //      "

        todoPage.getContent().forEach(System.out::println);
    }
}











