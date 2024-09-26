package edu.example.restz.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EntityNotFoundException extends RuntimeException {
    private String message;
    private int code;

    public EntityNotFoundException(String message) {
        super(message);
        this.message = message;
        this.code = 404;
    }
}
