package edu.example.restz.controller;

import edu.example.restz.dto.PageRequestDTO;
import edu.example.restz.dto.TodoDTO;
import edu.example.restz.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todo")
@Log4j2

//1. 컨트롤러 설명
@Tag(name="Todo Contoller", description = "오늘의 할 일 컨트롤러")
public class TodoController {
    private  final TodoService todoService;

    //2. 메서드 설명
    @Operation(summary = "오늘의 할 일 조회", description = "오늘의 할 일을 조회합니다.")
    @GetMapping("/{mno}")  //GET 요청으로 Todo 번호를 넘겨받아 조회
    public ResponseEntity<TodoDTO> read(
                            //3. 파라미터 설명
                            @Parameter(description = "조회할 번호를 입력하세요.")
                            @PathVariable("mno") Long mno) {
        log.info("mno : " + mno);
        return ResponseEntity.ok(todoService.read(mno));
    }


    @PostMapping("")  //Post 요청으로 TodoDTO를 전달받아    //등록
    public ResponseEntity<TodoDTO> register(
                    @Validated @RequestBody TodoDTO todoDTO){
        log.info("register() ----- " + todoDTO);         //로그로 출력
        return ResponseEntity.ok(todoService.register(todoDTO));
    }

    @GetMapping                         //목록
    public ResponseEntity<Page<TodoDTO>> getList(@Validated PageRequestDTO pageRequestDTO){
        log.info("getList() ----- " + pageRequestDTO);         //로그로 출력
        return ResponseEntity.ok(todoService.getList(pageRequestDTO));
    }

    @PutMapping("/{mno}")               //수정
    public ResponseEntity<TodoDTO> modify(@Validated @RequestBody TodoDTO todoDTO) {
        log.info("modify() : " + todoDTO);
        return ResponseEntity.ok(todoService.modify(todoDTO));
    }

    @DeleteMapping("/{mno}")            //삭제
    public ResponseEntity<Map<String, String>> remove(@PathVariable("mno") Long mno) {
        log.info("remove() : " + mno);
        todoService.remove(mno);        //삭제 처리 후
        Map<String, String> result = Map.of("result", "success");  //Map에 키는 result, 값은 success를 저장하여 ResponseEntity로 반환

        return ResponseEntity.ok(result);
    }


}













