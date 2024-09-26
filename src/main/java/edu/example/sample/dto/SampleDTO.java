package edu.example.sample.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor          //기본 생성자
@AllArgsConstructor         //모든 필드 초기화 생성자
//@RequiredArgsConstructor  //final, @NotNull 필드 초기화 생성자
public class SampleDTO {
    private Long ssn;
    private String name;
}
