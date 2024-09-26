package edu.example.restz.repository.search;

import edu.example.restz.dto.TodoDTO;
import edu.example.restz.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoSearch {
    Page<Todo> search(Pageable pageable);
    Page<TodoDTO> searchDTO(Pageable pageable);
}
