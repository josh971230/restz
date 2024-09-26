package edu.example.restz.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tbl_todo")
public class Todo {
    @Id                 //PK 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //AUTO_INCREMENT
    private Long mno;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String writer;

    private LocalDate dueDate;

    //setter - 엔티티 객체의 수정은 세터 대신 changeXXXX( ) 형태의 메서드로
    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeWriter(String writer){
        this.writer = writer;
    }

    public void changeDueDate(LocalDate dueDate){
        this.dueDate = dueDate;
    }
}
