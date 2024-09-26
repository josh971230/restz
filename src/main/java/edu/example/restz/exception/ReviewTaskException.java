package edu.example.restz.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewTaskException extends RuntimeException{
    private String message;
    private int code;
}
