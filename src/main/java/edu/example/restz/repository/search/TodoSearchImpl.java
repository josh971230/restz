package edu.example.restz.repository.search;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import edu.example.restz.dto.TodoDTO;
import edu.example.restz.entity.QTodo;
import edu.example.restz.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class TodoSearchImpl extends QuerydslRepositorySupport
                            implements TodoSearch {
    public TodoSearchImpl() {
        super(Todo.class);
    }

    @Override
    public Page<Todo> search(Pageable pageable) {
        QTodo todo = QTodo.todo;
        JPQLQuery<Todo> query = from(todo); //FROM todo
        query.where( todo.mno.gt(0L) );   //WHERE mno > 0

        getQuerydsl().applyPagination(pageable, query); //페이징 적용
        List<Todo> todoPage = query.fetch(); //쿼리 실행
        long count = query.fetchCount();  //레코드 수 조회

        return new PageImpl<>(todoPage, pageable, count);
    }

    @Override
    public Page<TodoDTO> searchDTO(Pageable pageable) {
        QTodo todo = QTodo.todo;
        JPQLQuery<Todo> query = from(todo); //FROM todo
        query.where( todo.mno.gt(0L) );   //WHERE mno > 0
        getQuerydsl().applyPagination(pageable, query); //페이징 적용

        JPQLQuery<TodoDTO> dtoQuery
                = query.select(Projections.constructor(TodoDTO.class, todo));   //생성자 방식 Projections
//        dtoQuery = query.select(Projections.bean( TodoDTO.class,      //bean 방식 Projections
//                            todo.mno, todo.title, todo.writer, todo.dueDate));

        List<TodoDTO> todoPage = dtoQuery.fetch(); //쿼리 실행
        long count = query.fetchCount();  //레코드 수 조회

        return new PageImpl<>(todoPage, pageable, count);
    }
}








