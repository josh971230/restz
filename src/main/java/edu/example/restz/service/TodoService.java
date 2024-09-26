package edu.example.restz.service;

import edu.example.restz.dto.PageRequestDTO;
import edu.example.restz.dto.TodoDTO;
import edu.example.restz.entity.Todo;
import edu.example.restz.exception.EntityNotFoundException;
import edu.example.restz.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service                    //1. 빈 등록
@RequiredArgsConstructor    //2. 생성자
@Transactional              //3. 트랜잭션
@Log4j2
public class TodoService {
    private final TodoRepository todoRepository;     //4.의존성 주입

    public TodoDTO register(TodoDTO todoDTO){   //등록
        Todo todo = todoDTO.toEntity();
        todoRepository.save(todo);
        return new TodoDTO(todo);
    }

    public TodoDTO read(Long mno) {             //조회
        Optional<TodoDTO> todoDTO = todoRepository.getTodoDTO(mno);
        return todoDTO.orElseThrow(() ->
                new EntityNotFoundException("Todo " + mno + " NOT FOUND")
        );           //값이 없으면 예외 발생
    }

    public void remove(Long mno) {  //삭제
        Optional<Todo> todo = todoRepository.findById(mno); //1. mno에 해당하는 데이터를 조회한 결과 저장
        Todo removeTodo = todo.orElseThrow(() ->            //2. 1의 결과가 없으면 EntityNotFoundException 발생 시키기
                               new EntityNotFoundException( "Todo " + mno + " NOT FOUND"));
        todoRepository.delete(removeTodo);          //3. mno에 해당하는 데이터 삭제 메서드 호출
    }

    public TodoDTO modify(TodoDTO todoDTO){     //수정
        Optional<Todo> todo = todoRepository.findById(todoDTO.getMno());
        Todo modifyTodo = todo.orElseThrow(() ->
                                new EntityNotFoundException( "Todo " +
                                        todoDTO.getMno() + " NOT FOUND"));

        //필요한 부분 수정 - 변경이 감지되면 수정 처리 수행
        modifyTodo.changeTitle(todoDTO.getTitle());
        modifyTodo.changeWriter(todoDTO.getWriter());
        modifyTodo.changeDueDate(todoDTO.getDueDate());

        return new TodoDTO(modifyTodo);
    }

    public Page<TodoDTO> getList(PageRequestDTO pageRequestDTO) { //목록
        Sort sort = Sort.by("mno").descending();
        Pageable pageable = pageRequestDTO.getPageable(sort);

        return todoRepository.searchDTO(pageable );
    }
}











