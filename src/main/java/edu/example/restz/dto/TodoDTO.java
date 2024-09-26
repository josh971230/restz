package edu.example.restz.dto;

import edu.example.restz.entity.Todo;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TodoDTO {
    private Long mno;

    @NotEmpty
    private String title;

    @NotEmpty
    private String writer;

    @FutureOrPresent
    private LocalDate dueDate;

    public TodoDTO(Todo todo) {
        this.mno = todo.getMno();
        this.title = todo.getTitle();
        this.writer = todo.getWriter();
        this.dueDate = todo.getDueDate();
    }

    public Todo toEntity(){
        return Todo.builder().mno(mno)
                             .title(title)
                             .writer(writer)
                             .dueDate(dueDate)
                             .build();
    }
}
